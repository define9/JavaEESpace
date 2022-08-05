package cn.com.syhu.dao;

import cn.com.syhu.entity.User;

public interface UserDao {
	User selectByUsername(String username);
	
	int insert(User user);
}
