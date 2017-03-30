package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.District;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by Thiru on 29/1/2017.
 */
public class DistrictMapper implements RowMapper<District>{
    public District mapRow(ResultSet rs, int rowNum) throws SQLException {
        District district = new District();
        district.setDistrictId(rs.getString("district_id"));
        district.setDistictName(rs.getString("district_name"));

        return district;

    }
}