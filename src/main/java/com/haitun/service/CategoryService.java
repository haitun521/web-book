package com.haitun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Category;
import com.haitun.dao.CategoryDao;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryDao dao;

	public Integer save(Category category){
		return dao.save(category);
	}
	
	public Category getByname(String name){
		
		return dao.get(name);
	}
}
