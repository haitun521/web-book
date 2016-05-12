package com.haitun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Comment;
import com.haitun.dao.CommentDao;

@Service
public class CommentService {

	@Autowired
	private CommentDao dao;

	public Integer save(Comment comment) {
		return dao.save(comment);
	}

	public List<Comment> getCommentList(int bookId) {
		return dao.selectCommentList(bookId);
	}

	public Integer add(Comment comment) {
		return dao.save(comment);
	}

}
