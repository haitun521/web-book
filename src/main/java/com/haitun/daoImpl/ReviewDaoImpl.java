package com.haitun.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Review;
import com.haitun.dao.ReviewDao;

@Repository
public class ReviewDaoImpl implements ReviewDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public int add(Review review) {
		getSession().save(review);
		return review.getId();
	}

	@Override
	public Review select(String userId, int bookId) {
		String hql = "from Review where user.id=? and book.id=? and deletedTime is null";
		return (Review) getSession().createQuery(hql).setString(0, userId)
				.setInteger(1, bookId).uniqueResult();
	}

	@Override
	public Review get(int id) {
		String hql = "from Review where id=? and deletedTime is null";

		return (Review) getSession().createQuery(hql).setInteger(0, id)
				.uniqueResult();
	}

	@Override
	public void update(Review review) {

		getSession().update(review);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Review> selectByuserId(String userId) {
		String hql = "from Review where user.id=? and deletedTime is null order by id desc";
		return (List<Review>) getSession().createQuery(hql).setString(0, userId).list();
	}

}
