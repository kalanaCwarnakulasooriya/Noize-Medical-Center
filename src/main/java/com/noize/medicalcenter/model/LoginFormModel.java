package com.noize.medicalcenter.model;

import com.noize.medicalcenter.dto.LoginFormDto;
import com.noize.medicalcenter.util.CrudUtil;

import java.sql.ResultSet;

public class LoginFormModel {
    public ResultSet btnLogin(LoginFormDto loginFormDto) throws Exception {
        String sql = "SELECT e.Name, e.Email, e.ContactNumber, e.Address, e.Role, u.Password " +
                "FROM user u " +
                "JOIN employee e ON u.UserId = e.UserId " +
                "WHERE u.Username = ?";

        ResultSet rst = CrudUtil.execute(sql, loginFormDto.getUsername());

        if (rst.next()) {
            return rst;
        }
        return null;
    }

}
