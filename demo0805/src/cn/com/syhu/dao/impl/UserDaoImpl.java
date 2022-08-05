package cn.com.syhu.dao.impl;

import cn.com.syhu.dao.UserDao;
import cn.com.syhu.entity.User;
import cn.com.syhu.util.DBUtil;

public class UserDaoImpl implements UserDao{
	
	private DBUtil dbUtil = new DBUtil();

	@Override
	public User selectByUsername(String username) {
		String sql = "SELECT * FROM `user` WHERE `username`=?";
		return dbUtil.selectOne(User.class, sql, username);
	}

	@Override
	public int insert(User user) {
		String sql = "INSERT INTO `user` VALUE(null,?,?,?,?)";
		return dbUtil.update(
				sql, 
				user.getUsername(), 
				user.getPassword(), 
				user.getRealname(), 
				user.getStatus()
		);
	}

}
