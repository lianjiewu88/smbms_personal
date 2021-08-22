package com.study.service.role;

import com.study.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleService {

    // 查询角色列表
    List<Role> getRoleList();
}
