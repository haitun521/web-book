package com.haitun.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Comment;
import com.haitun.dao.CommentDao;

@Repository
public class CommentDaoImpl implements CommentDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Integer save(Comment comment) {
		return (Integer) getSession().save(comment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> selectCommentList(int bookId) {
		
		String hql = "from Comment c where c.book.id=? order by c.date";
		return getSession().createQuery(hql).setInteger(0, bookId).list();
	}

}
