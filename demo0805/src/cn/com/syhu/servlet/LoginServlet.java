package cn.com.syhu.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.syhu.entity.User;
import cn.com.syhu.service.UserService;
import cn.com.syhu.service.impl.UserServiceImpl;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService = new UserServiceImpl();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		System.out.println(request.getRequestURI());
		System.out.println(request.getRequestURL());
		
		System.out.println(request.getScheme());
		System.out.println(request.getLocalAddr()); 
		System.out.println(request.getLocalPort());
		System.out.println(request.getServerPort());
		System.out.println(request.getServerName());
		System.out.println(request.getContextPath());
		
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		System.out.println("-----------------");
		System.out.println(username);
		System.out.println(password);
		
		// ?hobby=1&hobby=2
		String h = request.getParameter("hobby");
		System.out.println(h);
		String[] hobby = request.getParameterValues("hobby");
		System.out.println(Arrays.toString(hobby));
		
		Map<String, String[]> params = request.getParameterMap();
		System.out.println(params.keySet());
		System.out.println(params.values());
		
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println(name);
		}
		
		try {
			User user = userService.login(username, password);

			
			// session 存储
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
			// 3. 返回数据, 跳转到index.jsp, 无数据
			// 发送重定向: 向客户端浏览器,发送一个重定向的指令 302永久重定向
			response.sendRedirect("index.jsp");
		} catch (Exception e) {

			// e.printStackTrace();
			// 登录失败, 返回错误信息  跳转到login.jsp
			// 需要在跳转 login.jsp 页面时, 将登陆的错误信息传递过去
			// 加个参数
			System.out.println(e.getMessage());
			request.setAttribute("error", e.getMessage());
			// 请求转发
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
