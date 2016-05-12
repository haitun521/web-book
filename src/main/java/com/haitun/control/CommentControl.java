package com.haitun.control;

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
import com.haitun.bean.Comment;
import com.haitun.bean.Message;
import com.haitun.service.BookService;
import com.haitun.service.CommentService;
import com.haitun.util.RespCode;

@Controller
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
@ResponseBody
public class CommentControl {

	private static Logger logger = Logger.getLogger(UserControl.class);

	@Autowired
	private CommentService service;
	@Autowired
	private BookService bservice;

	@RequestMapping("/bookid/{id}")
	public String getCommentList(@PathVariable("id") int bookId) {

		logger.info("bookId===" + bookId);
		Message msg=null;
		List<Comment> lists = service.getCommentList(bookId);
		if (lists != null) {
			for (Comment comment : lists) {
				comment.setBook(null);
			}
			msg = new Message(RespCode.SUCCESS);
			msg.setItem(lists);
		}else{
			msg=new Message(RespCode.FAIL);
		}
		

		return JSON.toJSONString(msg);

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addComment(@RequestParam("bookid") int bookId,
			@RequestParam("content") String content) {
		logger.info(String.format("bookid=%d,comment=%s", bookId, content));

		Book book = bservice.get(bookId);
		Comment comment = new Comment(book, content, new Date());
		int index = service.add(comment);
		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(index);
		return JSON.toJSONString(msg);

	}

}
