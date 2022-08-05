<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	用户登录
	<form action="login">
		账号: <input type="text" name="username" value=""> <br> 
		密码: <input type="password" name="password" id=""> <br> 
		<input type="submit" value="提交">
		<div style="color:red">
			<!-- 获取Request对象中的error的属性值 -->
			<%
				// 内嵌Java代码
				String error = (String) request.getAttribute("error");
				if (error != null) {
					// 输出到页面中
					out.append(error);
				}
			%>
		</div>
	</form>
</body>
</html>