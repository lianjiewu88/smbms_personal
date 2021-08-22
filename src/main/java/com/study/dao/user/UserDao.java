package com.study.dao.user;

import com.study.pojo.Role;
import com.study.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    // 得到登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改当前用户密码
    public int updatePwd(Connection connection, int userId, String password) throws SQLException;

    // 根据用户名或者角色查询用户总数
    public int getUserAmount(Connection connection, String userName, int userRole) throws SQLException;

    // 根据条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException;

    // 增加一个用户
//    int addUser(Connection connection)

}
