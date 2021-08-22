package com.study.dao.bill;

import com.study.dao.BaseDao;
import com.study.pojo.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BillDaoImpl implements BillDao {

    public List<Bill> getBillList(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from smbms_bill";
        List<Bill> bills = null;
        if (connection != null) {
            bills = new LinkedList<Bill>();
            try {
                resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, null);
                while (resultSet.next()) {
                    Bill bill = new Bill();
                    bill.setId(resultSet.getInt("id"));
                    bill.setBillCode(resultSet.getString("billCode"));
                    bill.setProductName(resultSet.getString("productName"));
                    bill.setProductDesc(resultSet.getString("productDesc"));
                    bill.setProductUnit(resultSet.getString("productUnit"));
                    bill.setProductCount(resultSet.getBigDecimal("productCount"));
                    bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                    bill.setIsPayment(resultSet.getInt("isPayment"));
                    bill.setCreationDate(resultSet.getTimestamp("creationDate"));
                    bill.setCreatedBy(resultSet.getInt("createdBy"));
                    bill.setProviderId(resultSet.getInt("providerId"));
//                    bill.setProviderName(resultSet.getString("proName"));
                    bills.add(bill);
                }
                BaseDao.closeResources(null, preparedStatement, resultSet);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return bills;
    }
}
