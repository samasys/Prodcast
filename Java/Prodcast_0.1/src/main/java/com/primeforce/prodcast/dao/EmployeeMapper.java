package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sarathan732 on 4/22/2016.
 */
public class EmployeeMapper implements RowMapper<Employee> {

    public Employee mapRow(ResultSet rs, int rowNum ) throws SQLException{

        Employee emp = new Employee();
        emp.setEmployeeId(  rs.getLong( "employee_id"));
        emp.setUserId( rs.getString("user_name"));
        emp.setType( rs.getString("type"));
        emp.setFirstname( rs.getString("firstname"));
        emp.setLastname( rs.getString("lastname"));

        return emp;
    }

}
