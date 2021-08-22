package com.study.service.role;

import com.study.dao.BaseDao;
import com.study.dao.role.RoleDao;
import com.study.dao.role.RoleDaoImpl;
import com.study.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        RoleDao roleDao = new RoleDaoImpl();
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResources(connection, null, null);
        }
        return roleList;
    }
}
