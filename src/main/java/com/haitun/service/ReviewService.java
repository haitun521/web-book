package com.haitun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Review;
import com.haitun.dao.ReviewDao;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewDao dao;

	public int add(Review review) {
		
		return dao.add(review);
	}

	public Review select(String userId, int bookId) {
		return dao.select(userId,bookId);
	}

	public Review get(int id) {
		// TODO Auto-generated method stub
		return dao.get(id);
	}

	public void update(Review review) {

		dao.update(review);
	}

	public List<Review> getByUserId(String userId) {
		return dao.selectByuserId(userId);
	}

}
