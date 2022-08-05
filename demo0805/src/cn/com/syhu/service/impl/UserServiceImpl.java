package cn.com.syhu.service.impl;


import cn.com.syhu.common.UserConstant;
import cn.com.syhu.dao.UserDao;
import cn.com.syhu.dao.impl.UserDaoImpl;
import cn.com.syhu.entity.User;
import cn.com.syhu.service.UserService;

public class UserServiceImpl implements UserService{

	private UserDao userDao = new UserDaoImpl();
	
	@Override
	public User login(String username, String password) throws Exception {
		// 1. 参数校验֤
		if (	username == null ||
				username.length() < 3 ||
				username.length() > 16) {
			throw new Exception("账号必须 3 ~ 16 位");
		}
		if (	password == null ||
				password.length() < 3 ||
				password.length() > 16) {
			throw new Exception("密码必须 3 ~ 16 位");
		}
		
		// 2. 根据用户名查询
		User user = userDao.selectByUsername(username);
		
		// 3. 是否有此用户
		if (user == null) {
			throw new Exception("无此用户");
		}
		
		// 4. 校验密码
		if (!user.getPassword().equals(password)) {
			throw new Exception("密码错误");
		}
		
		// 5. 检查是否可用
		if (user.getStatus() != UserConstant.StatusEnum.AVAILABLE.getCode()) {
			throw new Exception("用户状态异常");
		}
		
		// 6. 返回用回信息
		return user;
	}

	@Override
	public void register(User user) throws Exception {
		// 1. 校验
		if (user == null) throw new Exception("数据为空");
		String username = user.getUsername();
		String password = user.getPassword();
		String realname = user.getRealname();
		
		if (	username == null ||
				username.length() < 3 ||
				username.length() > 16) {
			throw new Exception("用户名必须是 3 ~ 16 位");
		}
		
		if (	password == null ||
				password.length() < 3 ||
				password.length() > 16) {
			throw new Exception("密码必须是 3 ~ 16 位");
		}
		
		if ( realname != null && realname.length() > 32) {
			throw new Exception("真名太长");
		}
		
		// 2. 检验用户是否存在
		if (userDao.selectByUsername(username) != null) {
			throw new Exception("用户名已存在");
		}
		
		// 3. 更新状态
		user.setStatus(UserConstant.StatusEnum.AVAILABLE.getCode());
		
		if (1 != userDao.insert(user)) {
			throw new Exception("注册失败");
		}
	}

}
