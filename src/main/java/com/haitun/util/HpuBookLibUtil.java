package com.haitun.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.haitun.bean.Borrow;
import com.haitun.bean.HPULibBook;
import com.haitun.bean.User;
import com.haitun.service.BorrowService;
import com.haitun.service.UserService;

public class HpuBookLibUtil {

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

	public static boolean getUserInfo(String id, String name, UserService us,
			BorrowService bs) throws IOException, ParseException {

		CloseableHttpClient httpClient = invokeLogin(id, name);

		// 获取学生信息
		String url = "http://218.196.244.90:8080/dzff.php";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tday1", getTday()));
		parameters.add(new BasicNameValuePair("dzh", id));
		post.setEntity(new UrlEncodedFormEntity(parameters));

		Document document = getDocument(httpClient, post);

		Element elem = document.body().getElementById("disp1");
		if (elem == null) {
			return false;
		}

		Elements elements = elem.getElementsByTag("td");

		// 得到学生信息
		User user = new User(id, elements.get(6).html(),
				elements.get(8).html(), elements.get(10).html(), elements.get(
						14).html(), Float.parseFloat(elements.get(34).html()));
		us.save(user);

		// 获取借书信息
		url = "http://218.196.244.90:8080/dzjyls.php";
		HttpPost post1 = new HttpPost(url);
		List<NameValuePair> parameters1 = new ArrayList<NameValuePair>();
		parameters1.add(new BasicNameValuePair("dzhm", id));
		parameters1.add(new BasicNameValuePair("cxsj1", "2012-01-01"));
		parameters1.add(new BasicNameValuePair("cxsj2", "2017-01-01"));

		post1.setEntity(new UrlEncodedFormEntity(parameters1));

		document = getDocument(httpClient, post1);
		elements = document.body().getElementsByTag("table").get(13)
				.getElementsByTag("tr");

		for (int j = 2; j < elements.size(); j++) {

			Elements ele = elements.get(j).getElementsByTag("td");
			Borrow b = new Borrow(id, ele.get(1).html(), Timestamp.valueOf(ele
					.get(4).html() + " 00:00:00"));

			bs.save(b);
		}
		return true;

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

	/**
	 * 搜索图书
	 * 
	 * @return
	 * @throws Throwable
	 */
	public static List<HPULibBook> searchBook(String T1, String T2, String T3,
			String T4, String T5) throws Throwable {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			/*
			 * String url =
			 * "http://218.196.244.90:8080/bmls.php?T1=1&T2=1&T4=25&T3=9&T5=" +
			 * URLEncoder.encode("数据结构", "GB2312");
			 */
			String url = "http://218.196.244.90:8080/bmls.php?T1=" + T1
					+ "&T2=" + T2 + "&T4=" + T4 + "&T3=" + T3 + "&T5="
					+ URLEncoder.encode(T5, "GB2312");

			HttpGet get = new HttpGet(url);
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
								if (es.get(i).html().contains("CNY")) {
									String s = es.get(i).html()
											.replace("&nbsp;", " ")
											.replace("<br>", " ").trim();
									hpuLibBook.setISBNPrice(s);
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
								if (p + 2 <= es.size()) {
									String s = es.get(p + 2).ownText();
									if (!s.contains("读者号或借书证条码号:")) {
										hpuLibBook.setSummary(s);
									}
								}
							}

						}
					}
				}
			}
		}

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
}
