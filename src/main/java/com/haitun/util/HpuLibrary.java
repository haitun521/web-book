package com.haitun.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.expr.NewArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.format.datetime.DateFormatter;

import com.haitun.bean.Borrow;
import com.haitun.bean.HPULibBook;
import com.haitun.bean.User;
import com.haitun.service.BorrowService;
import com.haitun.service.UserService;

public class HpuLibrary {
	static ApplicationContext applicationContext = null;

	static UserService us;
	static BorrowService bs;

	/*
	 * static { applicationContext = new ClassPathXmlApplicationContext(
	 * "applicationContext.xml"); System.out.println(applicationContext); us =
	 * applicationContext.getBean(UserService.class); bs =
	 * applicationContext.getBean(BorrowService.class); }
	 */

	public static CloseableHttpClient invokeLogin(String id, String name)
			throws IOException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		String url = "http://218.196.244.90:8080/dz.php";
		HttpPost post = new HttpPost(url);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tday1", getTday()));
		parameters.add(new BasicNameValuePair("T1", id));
		parameters.add(new BasicNameValuePair("xm", name));
		post.setEntity(new UrlEncodedFormEntity(parameters));
		httpClient.execute(post);
		return httpClient;
	}

	public static void getUserInfo(String id, String name) throws IOException,
			ParseException {

		CloseableHttpClient httpClient = invokeLogin(id, name);

		// 获取学生信息
		String url = "http://218.196.244.90:8080/dzff.php";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tday1", getTday()));

		String num;
		System.out.println("姓名" + "\t\t" + "图书欠费");
		for (int i = 209; i <= 209; i++) {
			for (int k = 1; k < 2; k++) {
				for (int h = 2; h < 3; h++) {
					for (int g = 1; g < 41; g++) {

						num = "311" + i + "0" + k + "0" + h;
						if (String.valueOf(g).length() == 1) {
							num += "0";
						}
						num += g;
						parameters.add(new BasicNameValuePair("dzh", num));
						post.setEntity(new UrlEncodedFormEntity(parameters));

						Document document = getDocument(httpClient, post);

						Element elem = document.body().getElementById("disp1");
						if (elem == null) {
							System.out.println(document.body().toString());

							httpClient = invokeLogin("311209010224", "王德伟");
							parameters.add(new BasicNameValuePair("tday1",
									getTday()));
							g--;
							continue;
						}

						Elements elements = elem.getElementsByTag("td");
						if (elements.get(4).html().equals("")) {

							continue;
						}

						// 得到学生信息
						User user = new User(num, elements.get(6).html(),
								elements.get(8).html(),
								elements.get(10).html(), elements.get(14)
										.html(), Float.parseFloat(elements.get(
										34).html()));
						// us.save(user);

						System.out.println(user.getName() + "\t\t"
								+ user.getOverdue());

						// 获取借书信息
						url = "http://218.196.244.90:8080/dzjyls.php";
						HttpPost post1 = new HttpPost(url);
						List<NameValuePair> parameters1 = new ArrayList<NameValuePair>();
						parameters1.add(new BasicNameValuePair("dzhm", num));
						parameters1.add(new BasicNameValuePair("cxsj1",
								"2012-01-01"));
						parameters1.add(new BasicNameValuePair("cxsj2",
								"2017-01-01"));

						post1.setEntity(new UrlEncodedFormEntity(parameters1));

						document = getDocument(httpClient, post1);
						elements = document.body().getElementsByTag("table")
								.get(13).getElementsByTag("tr");

						for (int j = 2; j < elements.size(); j++) {

							Elements ele = elements.get(j).getElementsByTag(
									"td");
							Borrow b = new Borrow(num, ele.get(1).html(),
									Timestamp.valueOf(ele.get(4).html()
											+ " 00:00:00"));

							// bs.save(b);
						}
					}
				}
			}
		}

	}

	/**
	 * 解析html获取文档DOM
	 * 
	 * @param httpClient
	 * @param post
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static Document getDocument(HttpClient httpClient, HttpPost post) {
		CloseableHttpResponse response = null;
		InputStream is = null;
		Document document = null;
		try {
			response = (CloseableHttpResponse) httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			document = Jsoup.parse(is, "gbk", "http://218.196.244.90:8080/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 释放资源
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return document;
	}

	public static Document getDocument(HttpClient httpClient, HttpGet get) {
		CloseableHttpResponse response = null;
		InputStream is = null;
		Document document = null;
		try {
			response = (CloseableHttpResponse) httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			document = Jsoup.parse(is, "gbk", "http://218.196.244.90:8080/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 释放资源
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return document;
	}

	/**
	 * 获取tday1的值
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getTday() throws IOException {

		String url = "http://218.196.244.90:8080/dz1.php";
		Document document = Jsoup.connect(url).get();
		Elements elements = document.select("input[type=hidden]");
		return elements.get(0).attr("value");
	}

	public static List<HPULibBook> searchBook() throws Throwable {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			String url = "http://218.196.244.90:8080/bmls.php?T1=1&T2=1&T4=25&T3=9&T5="
					+ URLEncoder.encode("数据结构", "GB2312");

			HttpGet get = new HttpGet(url);

			/*
			 * HttpPost post = new HttpPost(url);
			 * 
			 * List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			 * parameters.add(new BasicNameValuePair("T1", "1"));
			 * parameters.add(new BasicNameValuePair("T2", "1"));
			 * parameters.add(new BasicNameValuePair("T3", "9"));
			 * parameters.add(new BasicNameValuePair("T4", "25"));
			 * parameters.add(new BasicNameValuePair("T5",
			 * URLEncoder.encode("围城", "GB2312"))); post.setEntity(new
			 * UrlEncodedFormEntity(parameters));
			 * 
			 * post.addHeader("Content-Type",
			 * "application/x-www-form-urlencoded"); post.addHeader("Referer",
			 * "http://218.196.244.90:8080/bml.php");
			 * post.addHeader("User-Agent",
			 * "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0"
			 * );
			 */
			try (CloseableHttpResponse resp = httpClient.execute(get)) {

				if (resp.getStatusLine().getStatusCode() == 200) {

					// System.out.println(EntityUtils.toString(resp.getEntity()));

					Document document = getDocument(httpClient, get);
					List<HPULibBook> lists = new ArrayList<HPULibBook>();
					Elements elements = document.getElementsByClass("TD2");
					if (elements != null) {
						for (Element elelemnt : elements) {
							Elements eles = elelemnt.select("td");

							if (eles != null) {
								HPULibBook book = new HPULibBook();

								book.setLink(eles.get(1).select("a").get(0)
										.attr("href"));
								book.setName(eles.get(1).select("a").get(0)
										.html());
								book.setAuthor(eles.get(2).html());

								book.setCallNumber(eles.get(3).select("a")
										.get(0).html()
										+ eles.get(3).ownText());
								book.setPublisher(eles.get(4).select("a")
										.get(0).html());
								book.setDateTime(eles.get(5).html());
								lists.add(book);
							}
						}
					}
					Elements elementsTD3 = document.getElementsByClass("TD3");
					if (elementsTD3 != null) {
						for (Element elelemnt : elementsTD3) {
							Elements eles = elelemnt.select("td");

							if (eles != null) {
								HPULibBook book = new HPULibBook();

								book.setLink(eles.get(1).select("a").get(0)
										.attr("href"));
								book.setName(eles.get(1).select("a").get(0)
										.html());
								book.setAuthor(eles.get(2).html());

								book.setCallNumber(eles.get(3).select("a")
										.get(0).html()
										+ eles.get(3).ownText());
								book.setPublisher(eles.get(4).select("a")
										.get(0).html());
								book.setDateTime(eles.get(5).html());
								lists.add(book);
							}
						}
					}

					if (lists.size() > 0) {
						getBookInfoList(lists);
						return lists;
					}

				}
			}
		}
		return null;

	}

	private static void getBookInfoList(List<HPULibBook> lists)
			throws Throwable {

		for (HPULibBook hpuLibBook : lists) {

			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				String rootUrl = "http://218.196.244.90:8080/";
				HttpGet get = new HttpGet(rootUrl + hpuLibBook.getLink());
				try (CloseableHttpResponse resp = httpClient.execute(get)) {

					if (resp.getStatusLine().getStatusCode() == 200) {
						Document doc = getDocument(httpClient, get);
						Element e = doc.select("table").get(5);
						Elements es = e.select("td[width=879]").get(0)
								.select("td");

						if (es != null && es.size() > 1) {
							
							int p = 0;
							for (int i = 1; i < es.size(); i++) {
								if(es.get(i).html().contains("CNY")){
									hpuLibBook.setISBNPrice(es.get(i).html());
								}
								
								if (es.get(i).html().contains("本书共")) {
									hpuLibBook.setBorrowInfo(es.get(i).html());
									p = i;
									break;
								}
							}
							if (p != 0) {
								List<String> addr = new ArrayList<String>();
								for (int i = p - 3; i < p; i++) {
									Elements ss = es.get(i).select("font");
									if (ss != null && ss.size() > 0) {
										addr.add(ss.get(0).ownText());
									}
								}
								hpuLibBook.setAddress(addr);
								hpuLibBook.setSummary(es.get(p + 2).ownText());
							}

						}
					}
				}
			}
		}

	}

	public static void main(String[] args) {
		/*
		 * try { HpuLibrary.getUserInfo("311201010728", "邹加宝");
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		try {
			List<HPULibBook> books = HpuLibrary.searchBook();
			if (books != null) {
				for (HPULibBook book : books) {
					System.out.println(book);
				}
			}

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
