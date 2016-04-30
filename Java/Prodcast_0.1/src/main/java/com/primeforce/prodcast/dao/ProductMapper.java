package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sarathan732 on 4/25/2016.
 */
public class ProductMapper implements RowMapper<Product> {

    public Product mapRow(ResultSet rs, int rowNum ) throws SQLException{
        Product product = new Product();
        product.setId( rs.getLong( "product_id" ));
        product.setProductName( rs.getString( "productname" ));
        product.setUnitPrice( rs.getFloat("unitprice"));
        product.setPriceType( rs.getString("price_type"));
        return product;
    }
}
