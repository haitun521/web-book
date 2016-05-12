package com.haitun.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.User;
import com.haitun.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void saveUser(User user) {

		getSession().save(user);
	}

	@Override
	public User getUser(String userId) {

		String hql = "from User where id=?";
		return (User) getSession().createQuery(hql).setString(0, userId)
				.uniqueResult();
	}

}
