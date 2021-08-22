package com.study.dao.role;

import com.study.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao  {
    // 查询用户角色表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
