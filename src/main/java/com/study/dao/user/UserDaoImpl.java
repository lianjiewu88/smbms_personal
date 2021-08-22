package com.study.dao.user;

import com.mysql.jdbc.StringUtils;
import com.study.dao.BaseDao;
import com.study.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    // 根据用户名或者角色查询用户总数
    public int getUserAmount(Connection connection, String userName, int userRole) throws SQLException {
        int userAmount = 0;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user as user,smbms_role as role where user.userRole = role.id ");

            ArrayList<Object> list = new ArrayList<Object>();//存放参数

            // 查询语句中是否只有userRole属性
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and user.userName like ? ");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append("and user.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();

            System.out.println("UserDaoImpl->getUserAmount::" + sql.toString());
            PreparedStatement preparedStatement = null;

            ResultSet resultSet = null;
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            if (resultSet.next()) {
                userAmount = resultSet.getInt("count");
                // 从结果集中获得数量
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return userAmount;
    }

    // 根据条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        List<User> userList = new LinkedList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select user.*,role.roleName as userRoleName from smbms_user as user,smbms_role as role where user.userRole = role.id ");

            ArrayList<Object> list = new ArrayList<Object>();//存放参数

            // 查询语句中是否只有userRole属性
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append("and user.userName like ? ");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append("and user.userRole = ?");
                list.add(userRole);
            }

            // 在数据库中，分页使用limit startIndex,pageSize
            // 0,5  1-5   1
            // 5,5  6-10  2
            // 10,5 11-15 3
            // 开始的索引 = （当前的页码 -1）* 每页显示的条数

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();

            System.out.println("UserDaoImpl->getUserList::" + sql.toString());
            PreparedStatement preparedStatement = null;

            ResultSet resultSet = null;
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return userList;
    }


}
