package cn.com.syhu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.syhu.entity.User;
import cn.com.syhu.service.UserService;
import cn.com.syhu.service.impl.UserServiceImpl;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	UserService userService = new UserServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.getWriter()
				.append("信息: 暂不支持 GET 请求, 请使用 POST 请求");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String realname = request.getParameter("realname");
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setRealname(realname);
		
		String msg = "";
		
		try {
			userService.register(user);
			msg = "注册成功";
		} catch (Exception e) {
			//e.printStackTrace();
			msg = e.getMessage();
		}
		
		response.setContentType("text/html; charset=utf-8");
		response.getWriter()
				.append("信息: ")
				.append(msg);
	}

}
