package com.haitun.dao;

import com.haitun.bean.User;

public interface UserDao {
	void saveUser(User user);
	User getUser(String userId);
}
