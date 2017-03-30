package com.primeforce.prodcast.dao;
import com.primeforce.prodcast.businessobjects.UserLogin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by Thiru on 11/1/2017.
 */
public class UserLoginMapper implements RowMapper<UserLogin>{
    public UserLogin mapRow(ResultSet rs, int rowCount) throws SQLException {
        UserLogin userLogin = new UserLogin();

        userLogin.setUId(rs.getLong("uid"));
        userLogin.setMobileNumber(rs.getString("mobile_no"));
        userLogin.setCountryId(rs.getString("country_id"));
        userLogin.setIsdCode(rs.getString("isd_code"));

        return userLogin;
    }
}