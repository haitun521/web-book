package com.haitun.dao;

import java.util.List;

import com.haitun.bean.Comment;

public interface CommentDao {
	
	Integer save(Comment comment);

	List<Comment> selectCommentList(int bookId);

}
