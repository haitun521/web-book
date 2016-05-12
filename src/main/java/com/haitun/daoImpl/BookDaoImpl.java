package com.haitun.daoImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.haitun.bean.Book;
import com.haitun.dao.BookDao;

@Repository
public class BookDaoImpl implements BookDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Integer save(Book book) {
		getSession().save(book);
		return book.getId();
	}

	@Override
	public Book select(int id) {
		// String hql="from Book where id=?";
		return (Book) getSession().get(Book.class, id);// createQuery(hql).setInteger(0,
														// id).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> selectBookListPage(int currentPage, int pageSize) {
		String hql = "from Book order by id desc";
		return getSession().createQuery(hql)
				.setFirstResult(currentPage * pageSize).setMaxResults(pageSize)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> selectBookByName(String name) {
		String hql = "from Book where name like ?";

		return getSession().createQuery(hql).setString(0, "%" + name + "%")
				.list();
	}

	@Override
	public Book selectBookByISBN(String isbn) {
		String hql="from Book where isbn=?";
		return (Book) getSession().createQuery(hql).setString(0, isbn)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Book> selectAllBooks() {
		String hql="from Book";
		return getSession().createQuery(hql).list();
	}

}
