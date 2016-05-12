package com.haitun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Book;
import com.haitun.dao.BookDao;

@Service
public class BookService {

	@Autowired
	private BookDao bookDao;
	
	public Integer save(Book book){
		return bookDao.save(book);
	}
	
	public Book get(int id){
		return bookDao.select(id);
	}

	public List<Book> getList(int currentPage, int pageSize) {
		
		return bookDao.selectBookListPage(currentPage,pageSize);
	}

	public List<Book> getListByName(String name) {
		// TODO Auto-generated method stub
		return bookDao.selectBookByName(name);
	}

	public Book getByISBN(String isbn) {
		// TODO Auto-generated method stub
		return bookDao.selectBookByISBN(isbn);
	}

	public List<Book> getAllBooks() {
		return bookDao.selectAllBooks();
	}
}
