package com.haitun.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haitun.bean.Book;
import com.haitun.bean.Booktags;
import com.haitun.bean.Category;
import com.haitun.bean.Comment;
import com.haitun.service.BookService;
import com.haitun.service.BooktagsService;
import com.haitun.service.CategoryService;
import com.haitun.service.CommentService;

public class DoubanAPI {

	/*
	 * static BookService bookService = null; static CategoryService
	 * categoryService = null; static BooktagsService booktagsService = null;
	 * static CommentService commentService = null;
	 * 
	 * static ApplicationContext context = null; static {
	 * 
	 * context = new ClassPathXmlApplicationContext(
	 * "classpath:applicationContext.xml"); bookService =
	 * context.getBean(BookService.class); categoryService =
	 * context.getBean(CategoryService.class); booktagsService =
	 * context.getBean(BooktagsService.class); commentService =
	 * context.getBean(CommentService.class);
	 * 
	 * }
	 */

	/**
	 * 通过豆瓣图书api保存图书
	 * 
	 * @throws Exception
	 */
	public static List<Book> saveBookFromDbByTag(String tag,
			BookService bookService, CategoryService categoryService,
			BooktagsService booktagsService, CommentService commentService)
			throws Exception {
		List<Book> lists = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://api.douban.com/v2/book/search?tag=" + tag
					+ "&start=0&count=10";
			HttpGet get = new HttpGet(url);
			HttpResponse req = client.execute(get);
			String data = praseEntity(req.getEntity().getContent());
			JSONObject json = JSON.parseObject(data);

			JSONArray arrays = json.getJSONArray("books");

			if (arrays != null) {
				lists = new ArrayList<Book>();
				for (Object obj : arrays) {
					lists.add(saveBook((JSONObject) obj, bookService,
							booktagsService, categoryService, commentService));
				}
			}
		}
		return lists;
	}

	public static Book saveBookFromDbByISBN(String isbn,
			BookService bookService, CategoryService categoryService,
			BooktagsService booktagsService, CommentService commentService)
			throws Exception {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://api.douban.com/v2/book/isbn/:" + isbn;
			HttpGet get = new HttpGet(url);
			HttpResponse req = client.execute(get);
			String data = praseEntity(req.getEntity().getContent());
			JSONObject json = JSON.parseObject(data);

			return saveBook(json, bookService, booktagsService,
					categoryService, commentService);

		}

	}

	public static Book saveBook(JSONObject json, BookService bookService,
			BooktagsService booktagsService, CategoryService categoryService,
			CommentService commentService) throws ParseException,
			ClientProtocolException, IOException {
		Book book = new Book();

		book.setName(json.getString("title"));
		book.setAuthor(json.getString("author"));
		book.setDate(json.getString("pubdate"));
		String image=json.getString("image");
		if(image.contains(".gif")){
			image="https://img3.doubanio.com/mpic/s23670575.jpg";
		}
		book.setImage(image);
		book.setIsbn(json.getString("isbn13"));
		String pages = json.getString("pages");
		if (!NumberUtils.isNumber(pages)) {

			for (int i = 0; i < pages.length(); i++) {
				if (pages.charAt(i) < '0' || pages.charAt(i) > '9') {
					pages = pages.substring(0, i);
					break;
				}
			}
		}
		if (pages.equals("")) {
			book.setPage(0);
		} else {
			book.setPage(Integer.parseInt(pages));
		}

		String price = json.getString("price");
		if (!NumberUtils.isDigits(price)) {
			if (price.contains("元")) {
				price = price.substring(0, price.indexOf("元"));
			} else {
				price = "99.90";
			}
		}
		book.setPrice(Float.parseFloat(price));
		book.setPublisher(json.getString("publisher"));
		book.setSummary(json.getString("summary"));
		bookService.save(book);

		JSONArray jsonArray = json.getJSONArray("tags");
		if (jsonArray != null && jsonArray.size() > 0) {

			for (Object object : jsonArray) {
				Category tag = null;
				String name = ((JSONObject) object).getString("title");
				tag = categoryService.getByname(name);
				if (tag == null) {
					tag = new Category();
					tag.setName(name);
					categoryService.save(tag);
				}
				Booktags booktags = new Booktags(tag, book);
				System.out.println(booktagsService.save(booktags));
			}
		}

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://api.douban.com/v2/book/"
					+ json.getInteger("id") + "/reviews";
			System.out.println(url);
			HttpGet get = new HttpGet(url);
			HttpResponse req = client.execute(get);
			String data = praseEntity(req.getEntity().getContent());
			JSONObject js = JSON.parseObject(data);

			JSONArray reviews = js.getJSONArray("reviews");
			if (reviews != null && reviews.size() > 0) {
				for (Object object : reviews) {
					Comment comment = new Comment(book,
							((JSONObject) object).getString("summary"),
							Timestamp.valueOf(((JSONObject) object)
									.getString("published")));// new
																// Utils("yyyy-MM-dd HH:mm:ss").String2Date(((JSONObject)
																// object).getString("published"))
					commentService.save(comment);
				}
			}
		}
		return book;
	}

	private static String praseEntity(InputStream is) throws IOException {
		// TODO Auto-generated method stub
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = is.read(buf)) != -1) {
			os.write(buf, 0, len);
		}
		return os.toString();
	}
}
