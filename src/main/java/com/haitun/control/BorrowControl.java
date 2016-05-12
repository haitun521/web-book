package com.haitun.control;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.haitun.bean.Borrow;
import com.haitun.bean.Message;
import com.haitun.service.BorrowService;
import com.haitun.util.RespCode;

@Controller
@ResponseBody
@RequestMapping(produces = "application/json;charset=UTF-8")
public class BorrowControl {

	private static Logger logger = Logger.getLogger(BorrowControl.class);

	@Autowired
	private BorrowService borrowService;

	@RequestMapping(value = "/borrows", method = RequestMethod.POST)
	public String getBorrowList(@RequestParam("sid") String studentId) {
		logger.info("studentId=" + studentId);

		List<Borrow> lists = borrowService.getBorrowListByStuId(studentId);
		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(lists);

		return JSON.toJSONString(msg);
	}

	@RequestMapping("/borrows/{id}")
	public String getBorrow(@PathVariable("id") Integer id) {
		logger.info("id=" + id);
		Borrow borrow = borrowService.getBorrow(id);

		Message msg = new Message(RespCode.SUCCESS);
		msg.setItem(borrow);

		return JSON.toJSONString(msg);
	}

}
