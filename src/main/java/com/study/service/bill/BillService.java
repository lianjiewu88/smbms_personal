package com.study.service.bill;

import com.study.pojo.Bill;

import java.util.List;

public interface BillService {
    // 查询订单
    List<Bill> selectBills();
}
