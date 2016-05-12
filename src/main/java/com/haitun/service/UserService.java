package com.haitun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haitun.bean.User;
import com.haitun.dao.UserDao;

@Service
public class UserService {
	
	@Autowired
	private UserDao userdao;
	
	public User get(String id){
		return userdao.getUser(id);
	}
	
	public void save(User user){
		userdao.saveUser(user);
	}

}
