package com.primeforce.prodcast.dao;
import com.primeforce.prodcast.businessobjects.UserRegistration;
import com.primeforce.prodcast.businessobjects.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Thiru on 28/1/2017.
 */

public class UserMapper implements RowMapper<UserRegistration> {


    private final boolean miniData ;

        public UserMapper(boolean miniData)
        {
            this.miniData = miniData;
        }

        public UserMapper()
        {
            this.miniData = true;
        }
        public UserRegistration mapRow(ResultSet rs, int rowNum ) throws SQLException{

            UserRegistration user = new UserRegistration();
            user.setName(rs.getString("name"));
            if( !miniData )
            {
                user.setMobilePhone(rs.getString("mobile_no"));
                user.setDoorNumber(rs.getString("door_number"));
                user.setStreet(rs.getString("street"));
                user.setTown(rs.getString("town"));
                user.setDistrict(rs.getString("district"));
                user.setuId(rs.getLong("uid"));


            }
            return user;
        }

    }


