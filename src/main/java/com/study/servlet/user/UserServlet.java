package com.study.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.study.dao.BaseDao;
import com.study.pojo.Role;
import com.study.pojo.User;
import com.study.service.role.RoleService;
import com.study.service.role.RoleServiceImpl;
import com.study.service.user.UserService;
import com.study.service.user.UserServiceImpl;
import com.study.util.Constants;
import com.study.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * servlet作用：
 * 1. 处理请求
 * 2. 调用业务
 * 3. 返回页面
 */
public class UserServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getParameter("method");
        if (!StringUtils.isNullOrEmpty(method) && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (!StringUtils.isNullOrEmpty(method) && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (!StringUtils.isNullOrEmpty(method) && method.equals("query")) {
            this.selectUser(req, resp);
        } else if (!StringUtils.isNullOrEmpty(method) && method.equals("add")) {
            this.addUser(req, resp);
        } else if (!StringUtils.isNullOrEmpty(method) && method.equals("getRoleList")) {
            this.getRoleList(req, resp);
        }
    }

    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(roleList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
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

    // 查询用户
    public void selectUser(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = new UserServiceImpl();
        RoleService roleService = new RoleServiceImpl();


        // 从前端获取数据
        String tempQueryUserRole = req.getParameter("queryUserRole");
        String queryUserName = req.getParameter("queryName");
        String tempPageIndex = req.getParameter("pageIndex");
        /*
        if (StringUtils.isNullOrEmpty(queryUserName)) {
            queryUserName = "";
        }*/
        int queryUserRole = 0;
        if (!StringUtils.isNullOrEmpty(tempQueryUserRole)) {
            queryUserRole = Integer.parseInt(tempQueryUserRole);
        }


        // 获取用户列表
        List<User> userList = null;
        // pageSize已经从配置文件中读取了
        // 配置文件中，方便后期进行修改
        int currentPageNo = 1;
        if (!StringUtils.isNullOrEmpty(tempPageIndex)) {

            try {
                currentPageNo = Integer.valueOf(tempPageIndex);
            } catch (Exception e) {
//                e.printStackTrace();
                try {
                    resp.sendRedirect("error.jsp");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        // 获取用户的总数
        int totalUser = userService.getUserAmount(queryUserName, queryUserRole);
        int pageSize = BaseDao.pageSize != null ? Integer.parseInt(BaseDao.pageSize) : 5;
        // 总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalUser);
        pageSupport.setCurrentPageNo(currentPageNo);
//        pageSupport.setTotalPageCount(totalUser);


        int totalPageCount = pageSupport.getTotalPageCount();
        // 控制首页和尾页
        // 如果页面要小于1了，就显示第一页的东西
        if (currentPageNo < 1)
            currentPageNo = 1;
            // 当前页面大于最后一页
        else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        // 获取用户列表展示
        userList = userService.getUserListByConditions(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("totalCount", totalUser);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 增加用户
    public void addUser(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String rUserPassword = req.getParameter("rUserPassword");
        String gender = req.getParameter("gender");
        // int
        String birthday = req.getParameter("birthday");
        // date
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        String userRole = req.getParameter("userRole");
        //int
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        if (StringUtils.isNullOrEmpty(userPassword) && StringUtils.isNullOrEmpty(rUserPassword) && userPassword.equals(rUserPassword))
            user.setUserPassword(userPassword);
        if (StringUtils.isNullOrEmpty(gender))
            user.setGender(Integer.parseInt(gender));
        if (StringUtils.isNullOrEmpty(birthday))
            user.setBirthday(Date.valueOf(birthday));
        user.setPhone(phone);
        user.setAddress(address);
        if (StringUtils.isNullOrEmpty(userRole))
            user.setUserRole(Integer.valueOf(userRole));
    }
}
