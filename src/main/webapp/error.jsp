<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<%--你要访问的页面，已经飞往火星！--%>
<a href="${pageContext.request.contextPath }/login.jsp">
    <img src="${pageContext.request.contextPath}/images/error.jpg" alt="错误页面" width="1000" height="600">
</a>
<%--<a href="javascript:window.history.back(-1);">返回</a>--%>

<%--这个地方的跳转有问题，应该能够直接跳转到登录页面就好了
我记得狂视频里面应该实现了这个，能够完成跳转--%>
</body>
</html>