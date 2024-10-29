package com.noize.medicalcenter.util;

import com.noize.medicalcenter.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object... obj) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        for (int i = 0; i < obj.length; i++) {
            pstm.setObject(i + 1, obj[i]);
        }

        try {
            if (sql.startsWith("SELECT") || sql.startsWith("select")) {
                ResultSet resultSet = pstm.executeQuery();
                return (T) resultSet;
            }else {
                int i = pstm.executeUpdate();
                boolean isDone = i > 0;
                return (T) ((Boolean) isDone);
            }
        } catch (Exception e) {
            throw new SQLException("Something went wrong : " + e.getMessage());
        }
    }
}
