package com.primeforce.prodcast.dao;

/**
 * Created by sarathan732 on 4/26/2016.
 */
public class DBSql {
    public final static String LOGIN_SQL = "select emp.employee_id, emp.firstname, emp.lastname, emp.type, emp_access.user_name  from employees emp, emp_access emp_access where emp_access.emp_id = emp.employee_id and emp_access.user_name=? and emp_access.password=?";
    public final static String CUSTOMER_SEARCH_SQL = "select customer_id, companyname from retailer_dtl where companyname like '";
    public final static String CUSTOMER_SQL = "select customer_id, companyname, billingaddress1, billingaddress2, billingaddress3, city, stateorprovince, postalcode, country_id, customer_type, area, contactfirstname, contactlastname, contacttitle, phonenumber, faxnumber, emailaddress, notes, wk_day  from retailer_dtl where customer_id=?";
    public final static String PRODUCT_SEARCH_SQL = " select product_id, productname, unitprice , price_type from products where productname like '";
    public final static String OUTSTANDING_BILL_SEARCH = "select oh.cust_id , oh.bill_no , oh.total_amt , oh.bill_date , oh.outstanding_bal  from  order_header oh where oh.cust_id = ? and oh.outstanding_bal >0 ";
    public final static String COLLECTION_UPDATE_SQL = "call sp_collection_update (? , ? , ? ) ";

    public final static String ORDER_DETAILS_SQL = "insert into order_dtl (orderdetailid, product_id , quantity, unitprice, amount) values( ? , ? , ?, (select unitprice from products where product_id = ?) , quantity*unitprice )";
    public final static String ORDER_TOTAL_SQL ="update order_header set total_amt = (select sum( amount) from order_dtl where orderdetailid = ? ) , outstanding_bal = total_amt where orderdetailid = ? ";

    public final static String FETCH_ORDER_SQL = " select oh.bill_date, oh.orderdetailid , oh.bill_no, oh.cust_id, cust.companyname, oh.emp_id , "+
    "emp.firstname , emp.lastname, oh.total_amt, oh.enter_dt_tm , oh.outstanding_bal "+
    "from order_header oh , employees emp , retailer_dtl cust where "+
    "oh.cust_id = cust.customer_id  and oh.emp_id = emp.employee_id and oh.bill_no=?";
    public final static String FETCH_ORDER_DTL_SQL = "select od.product_id , od.quantity , od.unitprice, od.amount , pr.productname from order_dtl od, products pr where od.product_id = pr.product_id and od.orderdetailid = ?";
    public final static String FETCH_ORDER_COLLECTION_SQL = "select cd.amount_paid, cd.emp_id , emp.firstname , emp.lastname, cd.payment_type, cd.payment_date, cd.enter_dt_tm from collection_dtl cd , employees emp where emp.employee_id = cd.emp_id and bill_no=?";


}
