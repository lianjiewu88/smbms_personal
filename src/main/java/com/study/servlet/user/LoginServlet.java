package com.study.servlet.user;

import com.study.pojo.User;
import com.study.service.user.UserService;
import com.study.service.user.UserServiceImpl;
import com.study.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    // Servlet层：控制层调用业务层代码

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet-start.....");

        // 获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        // 和数据库中的密码进行对比，调用业务层代码
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        if (user != null) {
            // 用户名和密码正确，可以登录
            // 将用户的信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            // 跳转到登录后的主页面
//            Cookie cookie = new Cookie("name","lianjie");
//            resp.addCookie(cookie);
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            // 转发回登录页面，并且携带错误信息
            req.setAttribute("error", "用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
