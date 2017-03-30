package com.primeforce.prodcast.dao;

/**
 * Created by Thiru on 7/1/2017.
 */
public class DataBaseSql
{
    public final static String FETCH_ALL_MOBILE_NUMBER = "select  * from census_person where country_id=? and mobile_no=? ";
    public final static String CUSTOMER_REGISTRATION = "insert into census_person (name,gender,date_of_birth,fathers_name,`mothers name`,race,education,occupation,door_number,street,town,district,taluk,state_id,country_id,mobile_no,email,aadhar_no,blood_group,pin_no) values(" +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public final static String ADMIN_REG = "insert into user_role (uid,role,mobile_no)values(?,?,?)";
    public final static String FETCH_NEW_REGISTRATION_COUNTRY = "select isd_code from country where country_id=?";

    public final static String LOGIN_SQL_NEW = "select emp.employee_id , emp.firstname , emp.lastname , emp.title , emp.email_id , emp.cellphone , emp.homephone, emp.workphone, emp.salary, emp.hire_dt, emp.termination_dt,emp.allowance, emp.address_1 , emp.address_2 , emp.address_3 , emp.city, emp.state , emp.postal_code, emp.country_id , emp.active_yn , emp.type , emp.location , emp.sex , emp.dob, emp.comments , emp.user_id , uaccess.email_id , uaccess.password, uaccess.user_role, uaccess.user_id,dst.cust_name,dst.timezone, ctry.country_id ,ctry.currency_symbol ,ctry.isd_code from employees emp , user_access uaccess,dist_dtl dst,country ctry where  dst.dist_id = emp.dist_manf_id and emp.employee_id = uaccess.employee_id and emp.email_id = ? and uaccess.password = ? and emp.country_id=ctry.country_id and dst.active_yn='1' and emp.active_yn='1' ";

   // public final static String GET_LOGIN = "select cns.name , cns.gender , cns.date_of_birth , cns.fathers_name , cns.`mothers name` , cns.race , cns.education, cns.occupation, cns.door_number, cns.street, cns.town,cns.district, cns.taluk , cns.state_id , cns.country_id , cns.mobile_no, cns.email , cns.aadhar_no, cns.blood_group , cns.pin_no ,cns.uid,usrole.role from census_person cns, user_role usrole  where cns.uid=usrole.uid and cns.country_id=? and cns.mobile_no=? and cns.pin_no=?";


    public final static String GET_LOGIN ="select * from census_person cns left join user_role urole on cns.uid=urole.user_id where cns.country_id=? and cns.mobile_no=? and cns.pin_no=?";

   //public final static String GET_LOGIN_U = "SELECT census_person.*, user_role.role FROM census_person LEFT JOIN user_role ON census_person.uid=user_role.uid ORDER BY census_person.name" ;

    public final static String UPDATE_SQL="update census_person set name=?,gender=?,date_of_birth=?,fathers_name=?,`mothers name`=?,race=?,education=?,occupation=?,door_number=?,street=?,town=?,district=?,taluk=?,state_id=?,country_id=?,mobile_no=?,email=?,aadhar_no=?,blood_group=? where uid=?";

    public final static String STATE_SQL = "select * from country_state ";

    public final static String DISTRICT_SQL = "select * from state_district";

    public final static String TALUK_SQL = "select * from district_taluk ";


    public final static String COUNTRY_SQL = "select * from country";


    public final static String GET_CUSTOMER_PHONENUMBER = "select pin_no from census_person where mobile_no = ? ";

    public final static String GET_ALL_REGISTORS = "select * from census_person cns left join user_role urole on cns.uid=urole.user_id";

    public final static String FETCH_MOBILE_NUMBER = "select  pin_no from census_person where country_id=? and mobile_no=? ";

    public final static String GET_USER_ROLE  = "select role from user_role where mobile_no = ?";

  //  public final static String GET_LOGIN ="FROM census_person LEFT JOIN user_role ON census_person.uid=user_role.uid ORDERr BY census_person.name";

    /*FROM Customers LEFT JOIN Orders
    ON Customers.CustomerID=Orders.CustomerID
    ORDER BY Customers.CustomerName;*/


}