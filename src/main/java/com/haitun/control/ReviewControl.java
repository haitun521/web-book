package com.haitun.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.haitun.bean.Message;
import com.haitun.bean.Review;
import com.haitun.bean.User;
import com.haitun.service.BookService;
import com.haitun.service.ReviewService;
import com.haitun.service.UserService;
import com.haitun.util.RespCode;

@RequestMapping(value = "/review", produces = "application/json;charset=UTF-8")
@Controller
@ResponseBody
public class ReviewControl {

	private static Logger logger = Logger.getLogger(ReviewControl.class);

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@RequestParam("userid") String userId,
			@RequestParam("bookid") int bookId) {

		logger.info(String.format("userId=%s,bookid=%d", userId, bookId));
		Message message = null;

		Review r = reviewService.select(userId, bookId);
		if (r == null) {
			Book book = bookService.get(bookId);
			User user = userService.get(userId);
			Review review = new Review(user, book, new Date(), null);
			int id = reviewService.add(review);
			if (id != 0) {
				message = new Message(RespCode.SUCCESS);
			} else {
				message = new Message(RespCode.FAIL);
			}
			message.setItem(id);
		} else {
			message = new Message(RespCode.SUCCESS);
		}
		return JSON.toJSONString(message);

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam("id") int id) {
		logger.info(String.format("id=%d", id));
		Message message = null;
		Review review = reviewService.get(id);
		if (review != null) {
			review.setDeletedTime(new Date());
			reviewService.update(review);
		}
		message = new Message(RespCode.SUCCESS);
		return JSON.toJSONString(message);
	}

	@RequestMapping("/get/{userid}")
	public String get(@PathVariable("userid") String userId) {
		logger.info(String.format("userId=%s", userId));
		Message message = null;
		List<Review> lists = reviewService.getByUserId(userId);
		if (lists != null&&lists.size()>0) {
			List<Book> books = new ArrayList<Book>();
			for (Review review : lists) {
				Book b = review.getBook();
				b.setBooktagses(null);
				b.setComments(null);
				b.setReviewId(review.getId());
				books.add(b);
			}
			message = new Message(RespCode.SUCCESS);
			message.setItem(books);
		}else{
			message = new Message(RespCode.FAIL);
		}	
		return JSON.toJSONString(message);
	}

}
