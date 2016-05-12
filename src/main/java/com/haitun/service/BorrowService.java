package com.haitun.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.Borrow;
import com.haitun.dao.BorrowDao;

@Service
public class BorrowService {

	@Autowired
	private BorrowDao borrowDao;

	public void save(Borrow borrow) {
		borrowDao.saveBorrow(borrow);
	}

	public List<Borrow> getBorrowListByStuId(String studentId) {

		return borrowDao.selectBorrowListByStuId(studentId);
	}

	public Borrow getBorrow(Integer id) {
		
		return borrowDao.selectBorrow(id);
	}

}
