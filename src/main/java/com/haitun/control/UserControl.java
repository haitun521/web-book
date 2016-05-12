package com.haitun.control;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.haitun.bean.Message;
import com.haitun.bean.User;
import com.haitun.service.BorrowService;
import com.haitun.service.UserService;
import com.haitun.util.HpuBookLibUtil;
import com.haitun.util.RespCode;

@RequestMapping(value="/user",produces="application/json;charset=UTF-8")
@Controller
@ResponseBody
public class UserControl {

	private static Logger logger = Logger.getLogger(UserControl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private BorrowService borrowService;

	@RequestMapping("/{id}")
	public String getUser(@PathVariable("id")String id) {

		User user = userService.get(id);
		Message message = new Message(RespCode.SUCCESS);
		message.setItem(user);
		return JSON.toJSONString(message);

	}

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(@RequestParam("id") String id,
			@RequestParam("name") String name) {

		logger.info(String.format("id= %s,name= %s", id, name));
		User user = userService.get(id);
		Message message = null;
		if (user != null) {
			if (user.getName().equals(name)) {
				message = new Message(RespCode.SUCCESS);
			} else {
				message = new Message(RespCode.FAIL);
			}
		} else {
			try {
				if (HpuBookLibUtil.getUserInfo(id, name, userService,
						borrowService)) {
					message = new Message(RespCode.SUCCESS);
				} else {
					message = new Message(RespCode.FAIL);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				message = new Message(RespCode.FAIL);
			}
		}
		return JSON.toJSONString(message);
	}

}
