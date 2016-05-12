package com.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.haitun.bean.Book;
import com.haitun.bean.Booktags;
import com.haitun.bean.Comment;
import com.haitun.bean.Message;
import com.haitun.bean.User;
import com.haitun.service.BookService;
import com.haitun.service.BooktagsService;
import com.haitun.service.BorrowService;
import com.haitun.service.CategoryService;
import com.haitun.service.CommentService;
import com.haitun.service.ReviewService;
import com.haitun.service.UserService;
import com.haitun.util.DoubanAPI;
import com.haitun.util.RespCode;

public class Test {

	static BookService bookService = null;
	static CategoryService categoryService = null;
	static BooktagsService booktagsService = null;
	static CommentService commentService = null;
	static ReviewService reviewService = null;

	static ApplicationContext context = null;
	static {

		context = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
		bookService = context.getBean(BookService.class);
		categoryService = context.getBean(CategoryService.class);
		booktagsService = context.getBean(BooktagsService.class);
		commentService = context.getBean(CommentService.class);
		reviewService = context.getBean(ReviewService.class);
	}

	@org.junit.Test
	public void test() throws ParseException {
		
		List<Book> books = new ArrayList<Book>(3);
		System.out.println(books.size());
		List<Book> lists = bookService.getAllBooks();
		getRandomBooks(lists, books);
		for (Book book : books) {
			System.out.println(book.getAuthor());
		}
		
		
		
		
		/*
		 * // fail("Not yet implemented"); List<Comment>
		 * lists=service.getCommentList(1);
		 * 
		 * System.out.println(lists.get(0).getBook()); Book
		 * book=bservice.get(1); System.out.println(book.getBooktagses());
		 * 
		 * @SuppressWarnings("unchecked") Iterator<Booktags>
		 * it=book.getBooktagses().iterator(); while(it.hasNext()){ Booktags
		 * b=it.next(); System.out.println(b.getCategory()); }
		 * System.out.println();
		 */
		/*try {
			//String url="https://api.douban.com/v2/book/search?tag=小说&start=0&count=2";
			String url="https://api.douban.com/v2/book/isbn/:9787510841774";
			Book b=DoubanAPI.saveBookFromDbByISBN("9787550013248", bookService, categoryService, booktagsService, commentService);
			System.out.println(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*List<Book> lists=null;
		
		@SuppressWarnings("unchecked")
		Set<Booktags> booktags= categoryService.getByname("计算机").getBooktagses();
		if(booktags!=null){
			lists=new ArrayList<Book>();
			Iterator<Booktags> it=booktags.iterator();
			while(it.hasNext()){
				Book b=it.next().getBook();
				b.setBooktagses(null);
				b.setComments(null);
				lists.add(b);
			}
		}
		
		if(lists!=null){
			System.out.println(lists.size());
		}*/
		
		/*Set<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		while (set.size() <3) {
			set.add(random.nextInt(59));
		}
		List<Integer> nums = new ArrayList<Integer>(set);
		for (Integer num : nums) {
			System.out.println(num);
		}*/
		/*List<Integer> lists=new ArrayList<Integer>();
		lists.add(3);
		lists.add(4);
		
		List<Integer> l=new ArrayList<Integer>();
		l.add(0);
		l.add(2);
		lists.addAll(1, l);
		
		for (Integer i : lists) {
			System.out.println(i);
		}
		*/
	}
	private void getRandomBooks(List<Book> lists, List<Book> books) {
		Set<Integer> set = new HashSet<Integer>();
		Random random = new Random();
		System.out.println(books.size());
		while (set.size() < 3) {
			set.add(random.nextInt(lists.size()-1));
		}
		System.out.println("=========");
		List<Integer> nums = new ArrayList<Integer>(set);
		for (Integer num : nums) {
			System.out.println(num);
			Book b = lists.get(num);
			b.setComments(null);
			b.setBooktagses(null);
			books.add(b);
		}
	}
}
