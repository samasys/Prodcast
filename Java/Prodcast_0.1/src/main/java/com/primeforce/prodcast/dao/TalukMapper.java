package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.Taluk;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by Thiru on 29/1/2017.
 */
public class TalukMapper  implements RowMapper<Taluk> {
    public Taluk mapRow(ResultSet rs, int rowNum) throws SQLException {
        Taluk taluk = new Taluk();
        taluk.setTalukId(rs.getString("taluk_id"));
        taluk.setTalukName(rs.getString("taluk_name"));

        return taluk;
    }
}
