package com.haitun.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Borrow;
import com.haitun.dao.BorrowDao;

@Repository
public class BorrowDaoImpl implements BorrowDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public void saveBorrow(Borrow Borrow) {

		getSession().save(Borrow);
	}

	@Override
	public Borrow selectBorrow(Integer id) {

		String hql = "from Borrow where id=?";

		return (Borrow) getSession().createQuery(hql).setInteger(0, id)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Borrow> selectBorrowListByStuId(String studentId) {

		String hql = "from Borrow where studentId=? order by id desc";

		return (List<Borrow>) getSession().createQuery(hql)
				.setString(0, studentId).list();
	}

}
