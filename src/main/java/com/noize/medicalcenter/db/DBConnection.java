package com.noize.medicalcenter.db;

import lombok.Getter;

import java.sql.*;

@Getter

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;
    private final String URL ="jdbc:mysql://localhost:3306/medicalcenter";
    private final String USER ="root";
    private final String PASSWORD ="Ijse@1234";

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
        if (dbConnection==null){
            dbConnection=new DBConnection();
        }
        return dbConnection;
    }
}

