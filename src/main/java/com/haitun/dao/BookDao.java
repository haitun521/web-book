package com.haitun.dao;

import java.util.List;

import com.haitun.bean.Book;

public interface BookDao {

	Integer save(Book book);

	Book select(int id);

	List<Book> selectBookListPage(int currentPage, int pageSize);

	List<Book> selectBookByName(String name);

	Book selectBookByISBN(String isbn);

	List<Book> selectAllBooks();
}
