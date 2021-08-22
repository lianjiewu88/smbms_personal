package com.study.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.study.pojo.User;
import com.study.service.user.UserService;
import com.study.service.user.UserServiceImpl;
import com.study.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getParameter("method");
        if (!StringUtils.isNullOrEmpty(method) && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (!StringUtils.isNullOrEmpty(method) && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    // 修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        // 从Session中拿取Id
        Object userObject = req.getSession().getAttribute(Constants.USER_SESSION);
        String rNewPassword = req.getParameter("rnewpassword");

        String newPassword = req.getParameter("newpassword");
        // 判断新密码和再次密码是否一致
        if (userObject != null && newPassword != rNewPassword && !StringUtils.isNullOrEmpty(newPassword)) {
            UserService userService = new UserServiceImpl();
            User user = (User) userObject;
            boolean flag = userService.updatePwd(user.getId(), newPassword);
            if (flag) {
                // 修改密码成功
                req.setAttribute(Constants.RETURN_MESSAGE, "修改密码成功，请退出，使用新密码再次登录");
                // 返回信息内容没有显示，如果直接进行跳转的话
                // 移除当前session
                req.getSession().removeAttribute(Constants.USER_SESSION);
//                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            } else {
                req.setAttribute("message", "密码修改失败");
//                resp.sendRedirect("");
            }
        } else {
            req.setAttribute("message", "新密码设置有误，请重新设置");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 验证旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object userObject = req.getSession().getAttribute(Constants.USER_SESSION);
        // 万能的map：结果集
        Map<String, String> resultMap = new HashMap<String, String>();
        String oldPassword = req.getParameter("oldpassword");
        if (userObject == null) {
            // session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldPassword)) {
            resultMap.put("result", "error");
        } else {
            User user = (User) userObject;
            // session中的用户密码
            String userPassword = user.getUserPassword();
            if (userPassword.equals(oldPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
