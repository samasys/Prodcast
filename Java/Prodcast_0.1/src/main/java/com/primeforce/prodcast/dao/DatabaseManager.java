package com.primeforce.prodcast.dao;

import com.primeforce.prodcast.businessobjects.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sarathan732 on 4/22/2016.
 */
@Component ("DatabaseManager")
public class DatabaseManager {


    private final JdbcTemplate template;

    @Autowired
    public DatabaseManager(JdbcTemplate template){
        this.template = template;
    }

    public Employee login(String userId, String password){
        return (Employee)template.queryForObject(DBSql.LOGIN_SQL, new Object[]{userId, password }, new EmployeeMapper());
    }

    public List<Customer> fetchCustomers(String startsWith){

        return template.query( DBSql.CUSTOMER_SEARCH_SQL + startsWith+"%'" , (Object[])null, new CustomerMapper());

    }

    public Customer getCustomer(String customerID){
        return template.queryForObject( DBSql.CUSTOMER_SQL , new Object[]{Long.parseLong( customerID) } , new CustomerMapper(false)  );
    }

    public List<Product> fetchProducts(String startsWith){
        return template.query( DBSql.PRODUCT_SEARCH_SQL +startsWith+"%'" , (Object[])null, new ProductMapper());
    }

    public List<Bill> fetchOutstandingBills(String customerID ){
        return template.query( DBSql.OUTSTANDING_BILL_SEARCH , new Object[]{Long.parseLong(customerID)} , new BillMapper() );
    }

    public int updateCollectionPayment(long dealerId, long billId, float amount ){
        return template.update( DBSql.COLLECTION_UPDATE_SQL , new Object[]{dealerId, billId , amount });
    }

    public boolean saveOrder(Order order, float paymentAmount){

        SimpleJdbcCall call = new  SimpleJdbcCall ( template.getDataSource() ).withProcedureName("sp_create_order");
        SqlParameterSource in = new MapSqlParameterSource().addValue("in_emp_id" , order.getEmployeeId() ).addValue("in_customer_id" , order.getCustomerId() );
        Map<String, Object> out = call.execute(in);

        long orderDetailId = (Long)out.get("out_orderdtl_id");
        long billNumber = (Long)out.get("out_bill_no");
        order.setOrderId( orderDetailId );
        order.setBillNumber( billNumber );
        for (OrderEntry entry:order.getOrderEntries()
             ) {
            template.update( DBSql.ORDER_DETAILS_SQL , new Object[]{orderDetailId , entry.getProductId(), entry.getQuantity() , entry.getProductId() });
        }

        template.update(DBSql.ORDER_TOTAL_SQL , new Object[]{orderDetailId, orderDetailId});

        if( paymentAmount > 0 ){
            updateCollectionPayment( order.getEmployeeId() , billNumber, paymentAmount );
        }
        return true;
    }

    public Order fetchOrder(long billId ){
        Order order = template.queryForObject( DBSql.FETCH_ORDER_SQL  , new Object[]{ billId } , new OrderMapper());

        order.setOrderEntries(template.query( DBSql.FETCH_ORDER_DTL_SQL , new Object[]{order.getOrderId()} , new OrderDetailMapper() ) );

        order.setCollectionEntries( template.query( DBSql.FETCH_ORDER_COLLECTION_SQL , new Object[]{billId} , new CollectionMapper()) );
;
        return order;
    }

 }
