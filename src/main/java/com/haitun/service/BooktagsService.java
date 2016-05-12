package com.haitun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Booktags;
import com.haitun.dao.BooktagsDao;

@Service
public class BooktagsService {

	@Autowired
	private BooktagsDao dao;
	
	public Integer save(Booktags tags){
		return dao.save(tags);
	}
}
