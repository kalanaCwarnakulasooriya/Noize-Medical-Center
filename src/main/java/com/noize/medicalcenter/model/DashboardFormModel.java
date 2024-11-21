package com.noize.medicalcenter.model;

import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DashboardFormModel {
    public HashMap<String, String> status() {
        HashMap<String,String> status = new HashMap<>();

        String sql = "select count(*) as count from ";
        String sql1 = "select count(*) as count from ";
        String sql2 = "select count(*) as count from ";
        String sql3 = "select count(*) as count from ";
        String sql4 = "select count(*) as count from ";
        String sql5 = "select count(*) as count from ";

        String[] tables = {"patient" , "doctor" , "prescription" , "appointment" , "orders" , "item"};
        String[] sqls = {sql , sql1 , sql2 , sql3, sql4, sql5};

        try{
            for (int i = 0; i <tables.length ; i++) {
                ResultSet rst = CrudUtil.execute(sqls[i] + tables[i]);
                if (rst.next()){
                    status.put(tables[i], rst.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
}
