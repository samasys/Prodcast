package com.primeforce.prodcast.dao;
import com.primeforce.prodcast.businessobjects.Country1;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Thiru on 12/1/2017.
 */
public class Country1Mapper implements RowMapper<Country1>
{
    public Country1 mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Country1 country = new Country1();
        country.setCountryId(rs.getString("country_id"));
        country.setCountryName(rs.getString("country_name"));

        return country;
    }
}
