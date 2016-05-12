package com.haitun.dao;

import java.util.List;

import com.haitun.bean.Review;

public interface ReviewDao {

	int add(Review review);

	Review select(String userId, int bookId);

	Review get(int id);

	void update(Review review);

	List<Review> selectByuserId(String userId);

}
