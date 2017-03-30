package com.primeforce.prodcast.dao;


import com.primeforce.prodcast.businessobjects.*;
import com.primeforce.prodcast.businessobjects.UserRegistration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Thiru on 10/1/2017.
 */
@Component ("DbManager")
public class DbManager {
    public final static String DATE_FORMAT = "dd/MM/yyyy";
    private final JdbcTemplate template1;

    @Autowired
    public DbManager(JdbcTemplate template1) {
        this.template1 = template1;
    }


    public List<UserRegistration> fetchMobileNumber1(String country, String mobilePhone) {
        Object[] args = new Object[]{country, mobilePhone};
        return template1.query(DataBaseSql.FETCH_ALL_MOBILE_NUMBER, args, new UserRegistrationMapper());


    }

    public List<Country1> fetchCountries() {

        return template1.query(DataBaseSql.COUNTRY_SQL, (Object[]) null, new Country1Mapper());

    }

    public List<State> fetchStates() {

        return template1.query(DataBaseSql.STATE_SQL, (Object[]) null, new StateMapper());

    }

    public List<District> fetchDistricts() {

        return template1.query(DataBaseSql.DISTRICT_SQL, (Object[]) null, new DistrictMapper());

    }

    public List<Taluk> fetchTaluks() {

        return template1.query(DataBaseSql.TALUK_SQL, (Object[]) null, new TalukMapper());

    }

    public int registerUser(String name, int gender, String dob, String fatherName, String motherName, String race, String education, String occupation, String doorname, String street, String town, String district, String taluk, String state, String country, String mobilePhone, String email, String aadhar, String bloodGroup, int code) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);


        Object[] args = new Object[]{name, gender, new Date(df.parse(dob).getTime()), fatherName,
                motherName, race, education, occupation, doorname, street,
                town, district, taluk, state, country, mobilePhone, email, aadhar, bloodGroup, code};
        return template1.update(DataBaseSql.CUSTOMER_REGISTRATION, args);


    }

    public String fetchCustomerCountryId1(String countryId) {
        return template1.queryForObject(DataBaseSql.FETCH_NEW_REGISTRATION_COUNTRY, new Object[]{countryId}, String.class);
    }


    public UserRegistration loginCustomer1(String mobilePhone, String country, int pin)
    {
        return template1.queryForObject(DataBaseSql.GET_LOGIN, new Object[]{country, mobilePhone, pin}, new UserRegistrationMapper());
    }


    public int updateCustomer1(String name, int gender, String dob, String fatherName, String motherName, String race, String education, String occupation, String doorname, String street, String town, String district, String taluk, String state, String country, String mobilePhone, String email, String aadhar, String bloodGroup, long registerId) throws ParseException {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Object[] args = new Object[]{name, gender, new Date(df.parse(dob).getTime()), fatherName,
                motherName, race, education, occupation, doorname, street,
                town, district, taluk, state, country, mobilePhone, email, aadhar, bloodGroup, registerId};
        return template1.update(DataBaseSql.UPDATE_SQL, args);

    }

    public int getPinFromMobile1(String mobilePhone) {
        try {

            return template1.queryForObject(DataBaseSql.GET_CUSTOMER_PHONENUMBER, new Object[]{mobilePhone}, Integer.class);
        } catch (Exception er) {
            System.out.println(er.toString());
            return -1;
        }
    }


    public List<UserRegistration> getAllRegistors1() {
        return template1.query(DataBaseSql.GET_ALL_REGISTORS, (Object[]) null, new UserRegistrationMapper());
    }



    public List<UserRegistration> fetchMobileNumber2(String country, String mobilePhone) {
        Object[] args = new Object[]{country, mobilePhone};
        return template1.query(DataBaseSql.FETCH_MOBILE_NUMBER, args, new UserRegistrationMapper());
    }



    }


