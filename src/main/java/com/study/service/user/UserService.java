package com.study.service.user;

import com.study.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    // 用户登录
    User login(String userCode, String password);

    // 根据用户ID修改密码
    public boolean updatePwd(int userId, String password);

    // 查询用户数量
    int getUserAmount(String userName, int userRole);

    // 根据条件查询用户列表
    List<User> getUserListByConditions(String userName, int userRole, int currentPageNo, int pageSize);


}
