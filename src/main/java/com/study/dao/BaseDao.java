package com.study.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 操作数据库的公共类
public class BaseDao {

    private static String driver;
    private static String url;
    private static String userName;
    private static String password;
    public static String pageSize;

    // 静态代码块，类加载时进行初始化
    static {
        InputStream inputStream = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
            inputStream = BaseDao.class.getClassLoader().getResourceAsStream("pagehelper.properties");
            properties.load(inputStream);
            pageSize = properties.getProperty("pageSize");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 获取数据库的连接
    public static Connection getConnection() {
        Connection connection = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName(driver);
            // 之所以写forName()是为了使用类加载器加载Driver类，从而运行其内部的静态代码块
            // 下面的DriverManager实现注册驱动的作用
            // 但是jdbc4.0以后，在META-INF\services\java.sql.Driver里面有声明，所以即使不写也可以
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // 查询公共方法
    public static ResultSet execute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet, String sql, Object[] params) throws SQLException {
        // 预编译的sql，在后面直接执行就可以了
        preparedStatement = connection.prepareStatement(sql);
        if (params != null)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    // 增删改公共方法
    public static Integer execute(Connection connection, PreparedStatement preparedStatement, String sql, Object[] params) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
    }

    // 释放资源
    public static boolean closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean flag = true;
        if (resultSet != null) {
            try {
                resultSet.close();
                // GC回收
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }

        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
                // GC回收
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }

        }

        if (connection != null) {
            try {
                connection.close();
                // GC回收
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }

        }
        return flag;
    }
}
