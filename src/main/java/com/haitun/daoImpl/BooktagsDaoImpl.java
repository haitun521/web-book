package com.haitun.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Booktags;
import com.haitun.dao.BooktagsDao;

@Repository
public class BooktagsDaoImpl implements BooktagsDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	@Override
	public Integer save(Booktags tags) {
		return (Integer) getSession().save(tags);
	}

}
