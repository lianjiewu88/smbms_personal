package com.study.dao.user;

import com.study.dao.BaseDao;
import com.study.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getTimestamp("creationDate"));
                user.setModifiedBy(resultSet.getInt("modifiedBy"));
                user.setModifyDate(resultSet.getTimestamp("modifiedDate"));
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return user;
    }

    public int updatePwd(Connection connection, int userId, String password) throws SQLException {

        int result = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            PreparedStatement preparedStatement = null;
            Object[] params = {password, userId};
            result = BaseDao.execute(connection, preparedStatement, sql, params);
            BaseDao.closeResources(null, preparedStatement, null);
        }

        return result;
    }
}
