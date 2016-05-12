package com.haitun.control;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.haitun.bean.Book;
import com.haitun.bean.Booktags;
import com.haitun.bean.HPULibBook;
import com.haitun.bean.Message;
import com.haitun.bean.Review;
import com.haitun.service.BookService;
import com.haitun.service.BooktagsService;
import com.haitun.service.CategoryService;
import com.haitun.service.CommentService;
import com.haitun.service.ReviewService;
import com.haitun.util.DoubanAPI;
import com.haitun.util.HpuBookLibUtil;
import com.haitun.util.RespCode;

@Controller
@RequestMapping(value = "/book", produces = "application/json;charset=UTF-8")
@ResponseBody
public class BookControl {

	private static Logger logger = Logger.getLogger(UserControl.class);

	@Autowired
	private BookService bookService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private BooktagsService booktagsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ReviewService reviewService;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchBook(@RequestParam("T1") String T1,
			@RequestParam("T2") String T2, @RequestParam("T3") String T3,
			@RequestParam("T4") String T4, @RequestParam("T5") String T5) {
		logger.info(String.format("T1=%s,T2=%s,T3=%s,T4=%s,T5=%s", T1, T2, T3,
				T4, T5));
		Message msg = null;
		try {
			List<HPULibBook> lists = HpuBookLibUtil.searchBook(T1, T2, T3, T4,
					T5);
			msg = new Message(RespCode.SUCCESS);
			msg.setItem(lists);
		} catch (Throwable e) {
			msg = new Message(RespCode.FAIL);
			logger.info("系统异常", e);
			e.printStackTrace();
		}

		return JSON.toJSONString(msg);

	}

	@RequestMapping(value = "/get")
	public String getBookList(@RequestParam("currentPage") int currentPage,
			@RequestParam("pageSize") int pageSize) {
		logger.info(String.format("currentPage=%d,pageSize=%d", currentPage,
				pageSize));

		List<Book> lists = bookService.getList(currentPage, pageSize);

		if (lists != null) {
			for (Book book : lists) {

				book.setBooktagses(null);
				book.setComments(null);

			}
		}
		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(lists);
		return JSON.toJSONString(msg);

	}

	@RequestMapping(value = "/{id}")
	public String getBook(@PathVariable("id") int id) {
		logger.info("id=" + id);
		Message msg = null;
		Book book = bookService.get(id);
		if (book != null) {

			List<String> categoryNames = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Iterator<Booktags> it = book.getBooktagses().iterator();
			while (it.hasNext()) {
				categoryNames.add(it.next().getCategory().getName());
			}
			book.setBooktagses(null);
			book.setComments(null);
			book.setCategoryNames(categoryNames);
			msg = new Message(RespCode.SUCCESS);
			msg.setItem(book);
		} else {
			msg = new Message(RespCode.FAIL);
		}

		return JSON.toJSONString(msg);
	}

	@RequestMapping(value = "/name/{name}")
	public String getBookListByName(@PathVariable("name") String name) {
		logger.info("name=" + name);

		List<Book> lists = bookService.getListByName(name);

		if (lists != null) {
			for (Book book : lists) {

				book.setBooktagses(null);
				book.setComments(null);

			}
		}
		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(lists);
		return JSON.toJSONString(msg);

	}

	@RequestMapping(value = "/isbn/{isbn}")
	public String getBookByISBN(@PathVariable("isbn") String isbn) {
		logger.info("isbn=" + isbn);

		Message msg = null;
		Book book = bookService.getByISBN(isbn);
		if (book != null) {

			List<String> categoryNames = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Iterator<Booktags> it = book.getBooktagses().iterator();
			while (it.hasNext()) {
				categoryNames.add(it.next().getCategory().getName());
			}
			book.setBooktagses(null);
			book.setComments(null);
			book.setCategoryNames(categoryNames);
			msg = new Message(RespCode.SUCCESS);
			msg.setItem(book);
		} else {
			try {
				Book b = DoubanAPI.saveBookFromDbByISBN(isbn, bookService,
						categoryService, booktagsService, commentService);
				msg = new Message(RespCode.SUCCESS);
				msg.setItem(b);
			} catch (Exception e) {
				logger.info("系统异常", e);
				msg = new Message(RespCode.FAIL);
				e.printStackTrace();
			}
		}

		return JSON.toJSONString(msg);
	}

	@RequestMapping(value = "/category/{name}")
	public String getBookListByCategory(
			@PathVariable("name") String CategoryName) {
		logger.info("CategoryName=" + CategoryName);
		List<Book> lists = null;
		@SuppressWarnings("unchecked")
		Set<Booktags> booktags = categoryService.getByname(CategoryName)
				.getBooktagses();
		if (booktags != null) {
			lists = new ArrayList<Book>();
			Iterator<Booktags> it = booktags.iterator();
			while (it.hasNext()) {
				Book b = it.next().getBook();
				b.setBooktagses(null);
				b.setComments(null);
				if (!lists.contains(b)) {
					lists.add(b);
				}
			}
		}
		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(lists);
		return JSON.toJSONString(msg);

	}

	@RequestMapping(value = "/recommend", method = RequestMethod.POST)
	public String getRecommendBookList(@RequestParam("userid") String userId) {
		logger.info("userid=" + userId);
		List<Book> books = new ArrayList<Book>(3);

		if (userId == null || userId.equals("")) {
			List<Book> lists = bookService.getAllBooks();
			getRandomBooks(lists, books);
		} else {
			List<Review> reviews = reviewService.getByUserId(userId);
			if (reviews == null || reviews.size() < 10) {
				List<Book> lists = null;
				@SuppressWarnings("unchecked")
				Set<Booktags> booktags = categoryService.getByname("计算机")
						.getBooktagses();
				if (booktags != null) {
					lists = new ArrayList<Book>();
					Iterator<Booktags> it = booktags.iterator();
					while (it.hasNext()) {
						Book b = it.next().getBook();
						b.setBooktagses(null);
						b.setComments(null);
						if (!lists.contains(b)) {
							lists.add(b);
						}
					}
				}
				getRandomBooks(lists, books);
			}else{
				
			}
		}

		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(books);
		return JSON.toJSONString(msg);

	}

	private void getRandomBooks(List<Book> lists, List<Book> books) {
		Set<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		while (set.size() < 3) {
			set.add(random.nextInt(lists.size()-1));
		}
		List<Integer> nums = new ArrayList<Integer>(set);
		for (Integer num : nums) {
			Book b = lists.get(num);
			b.setComments(null);
			b.setBooktagses(null);
			books.add(b);
		}
	}

}
