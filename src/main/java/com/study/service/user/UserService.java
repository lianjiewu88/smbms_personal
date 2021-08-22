package com.study.service.user;

import com.study.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserService {
    // 用户登录
    User login(String userCode, String password);

    // 根据用户ID修改密码
    public boolean updatePwd(int userId, String password);
}
