package com.study.dao.bill;

import com.study.pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {
    List<Bill> getBillList(Connection connection);
}
