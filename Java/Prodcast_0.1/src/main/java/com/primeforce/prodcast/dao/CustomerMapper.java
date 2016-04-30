package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.Customer;
import com.primeforce.prodcast.businessobjects.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sarathan732 on 4/22/2016.
 */
public class CustomerMapper implements RowMapper<Customer> {

    private final boolean miniData ;

    public CustomerMapper(boolean miniData){
        this.miniData = miniData;
    }

    public CustomerMapper(){
        this.miniData = true;
    }
    public Customer mapRow(ResultSet rs, int rowNum ) throws SQLException{

        Customer cust = new Customer();
        cust.setId( rs.getLong( "customer_id"));
        cust.setCustomerName(rs.getString("companyname"));
        if( !miniData ) {
            cust.setBillingAddress1(rs.getString("billingaddress1"));
            cust.setBillingAddress2(rs.getString("billingaddress2"));
            cust.setBillingAddress3(rs.getString("billingaddress3"));
            cust.setCity(rs.getString("city"));
            cust.setCountry(rs.getString("country_id"));
            cust.setPostalCode(rs.getString("postalcode"));
            cust.setState(rs.getString("stateorprovince"));

            cust.setCustomerType( rs.getString("customer_type"));
            cust.setArea(rs.getString("area"));
            cust.setFirstname( rs.getString("contactfirstname"));
            cust.setLastname( rs.getString("contactlastname"));
            cust.setTitle(rs.getString( "contacttitle"));
            cust.setPhonenumber( rs.getString("phonenumber"));
            cust.setFaxnumber( rs.getString("faxnumber"));
            cust.setNotes( rs.getString("notes"));
            cust.setEmailaddress( rs.getString( "emailaddress"));
            cust.setWeekday( rs.getString( "wk_day"));


        }
        return cust;
    }

}
