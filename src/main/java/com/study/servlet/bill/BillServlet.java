package com.study.servlet.bill;

import com.study.pojo.Bill;
import com.study.service.bill.BillService;
import com.study.service.bill.BillServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.sendRedirect(req.getContextPath() + "/jsp/billlist.jsp");
        BillService billService = new BillServiceImpl();
        List<Bill> bills = billService.selectBills();
        req.setAttribute("billList", bills);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
