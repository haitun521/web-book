package com.haitun.dao;

import com.haitun.bean.Category;

public interface CategoryDao {

	Integer save(Category category);
	Category get(String name);
}
