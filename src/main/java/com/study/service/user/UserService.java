package com.study.service.user;

import com.study.pojo.User;

public interface UserService {
    // 用户登录
    User login(String userCode, String password);
}
