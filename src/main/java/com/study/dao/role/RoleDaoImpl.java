package com.study.dao.role;

import com.study.dao.BaseDao;
import com.study.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from smbms_role";
        List<Object> params = new ArrayList<Object>();
        List<Role> roleList = new ArrayList<Role>();
        if (connection != null) {
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params.toArray());
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id"));
                role.setRoleCode(resultSet.getString("roleCode"));
                role.setRoleName(resultSet.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return roleList;
    }
}
