<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!-- contentType 服务器解析JSP编码格式 -->
<!-- pageEncoding JSP文件保存时编码格式, 若为ISO8859-1,文件内有中文保存失败 -->
<%@page import="java.util.Date" %>
<%@page import="cn.com.syhu.entity.*" %>
<%
    // 内嵌JAVA代码
    Date date = new Date();
%>

<%!
    int a = 12;

    public int max(int a, int b) {
        return a > b ? a : b;
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- content 浏览器解析编码 -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<%
    User user = (User) session.getAttribute("user");

    if (user == null) {
        //out.write("<a href='login.jsp'>登录</a>");
%>
<a href="login.jsp">登录</a>
<%
} else {
    out.write("欢迎: [" + user.getUsername() + "]");
%>
<a href="logout">退出</a>
<%
    }
%>

This is my first JSP page! 这是我的第一个JSP页面
<%@include file="inner.jsp" %>
</body>
</html>