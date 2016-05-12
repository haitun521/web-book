package com.haitun.dao;

import java.util.List;

import com.haitun.bean.Borrow;

public interface BorrowDao {
	void saveBorrow(Borrow borrow);

	Borrow selectBorrow(Integer id);

	List<Borrow> selectBorrowListByStuId(String studentId);
}
