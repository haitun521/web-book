package com.haitun.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Category;
import com.haitun.dao.CategoryDao;

@Repository
public class CategoryDaoImpl implements CategoryDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public Integer save(Category category) {
		getSession().save(category);
		return category.getId();
	}

	@Override
	public Category get(String name) {
		String hql="from Category where name=?";
		return (Category) getSession().createQuery(hql).setString(0, name).uniqueResult();
	}

}
