package cn.com.syhu.service;

import cn.com.syhu.entity.User;

public interface UserService {
	
	User login(String username, String password) throws Exception;

	void register(User user) throws Exception;
}
