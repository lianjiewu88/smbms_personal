package com.study.service.bill;

import com.study.dao.BaseDao;
import com.study.dao.bill.BillDao;
import com.study.dao.bill.BillDaoImpl;
import com.study.pojo.Bill;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class BillServiceImpl implements BillService {

    private BillDao billDao;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    public List<Bill> selectBills() {
        Connection connection = BaseDao.getConnection();
        List<Bill> bills = billDao.getBillList(connection);
        BaseDao.closeResources(connection, null, null);
        return bills;
    }

    @Test
    public void test() {
        BillService billService = new BillServiceImpl();
        List<Bill> bills = billService.selectBills();
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }
}
