package com.study.dao.user;

import com.study.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao {

    // 得到登录的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改当前用户密码
    public int updatePwd(Connection connection, int userId, String password) throws SQLException;
}
