package com.noize.medicalcenter.model;

import com.noize.medicalcenter.db.DBConnection;
import com.noize.medicalcenter.dto.OrderDetailsFormDto;
import com.noize.medicalcenter.dto.OrdersFormDto;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersFormModel {
    private final OrderDetailsFormModel orderDetailsFormModel = new OrderDetailsFormModel();

    public String getNewOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT OrderId FROM orders ORDER BY OrderId DESC LIMIT 1");

        if (rst.next()) {
            int lastId = rst.getInt("OrderId");
            int newIdIndex = lastId + 1;
            return String.format("%1d", newIdIndex);
        }
        return "O001";
    }

    public boolean saveOrder(OrdersFormDto orderDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute(
                    "insert into orders values (?,?,?)",
                    orderDTO.getOrderId(),
                    orderDTO.getOrderDate(),
                    orderDTO.getPatientId()
            );

            if (isOrderSaved) {
                boolean isOrderDetailListSaved = orderDetailsFormModel.saveOrderDetailsList(orderDTO.getOrderDetailsFormDtos());
                if (isOrderDetailListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
