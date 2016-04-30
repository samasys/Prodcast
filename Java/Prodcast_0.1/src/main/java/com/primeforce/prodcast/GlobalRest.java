package com.primeforce.prodcast;

import com.primeforce.prodcast.businessobjects.Area;
import com.primeforce.prodcast.businessobjects.Customer;
import com.primeforce.prodcast.businessobjects.Employee;
import com.primeforce.prodcast.businessobjects.Product;
import com.primeforce.prodcast.dao.DatabaseManager;
import com.primeforce.prodcast.dao.Order;
import com.primeforce.prodcast.dao.OrderEntry;
import com.primeforce.prodcast.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sarathan732 on 4/22/2016.
 */
@Named
@Path("/global/")
public class GlobalRest {


    private final DatabaseManager databaseManager;

    @Autowired
    public GlobalRest(DatabaseManager manager) {
        databaseManager = manager;
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginDTO authenticate(@QueryParam("userid") String id, @QueryParam("password") String password) {

        LoginDTO dto = new LoginDTO();
        Employee employee = null;

        try {
            employee = databaseManager.login(id,password);
            if( employee == null ) dto.setSuccess( false );
            else dto.setSuccess(true);
            dto.setEmployee(employee);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

    @POST
    @Path("loginp")
    @Produces(MediaType.APPLICATION_JSON)
    public LoginDTO authenticatePost(@RequestBody UserDTO user) {

        LoginDTO dto = new LoginDTO();
        Employee employee = null;

        try {
            employee = databaseManager.login(user.getUserId(),user.getPassword());
            if( employee == null ) dto.setSuccess( false );
            else dto.setSuccess(true);
            dto.setEmployee(employee);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @GET
    @Path("areas")
    @Produces(MediaType.APPLICATION_JSON)
    public AreaDTO getAreas(@QueryParam("id") String dealerId) {

            Area area = new Area();
            area.setId(1);
        area.setDescription("Adyar");

            List<Area> areas = new LinkedList<Area>();
        areas.add( area );

        area = new Area();
        area.setId(2);
        area.setDescription("Thiruvanmiyur");
        areas.add( area );

        AreaDTO dto = new AreaDTO();
        dto.setAreas(areas );

        return dto;

    }

    @GET
    @Path("customers")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerListDTO getCustomerList(@QueryParam("startswith") String id) {
        CustomerListDTO dto = new CustomerListDTO();

        try {
            List customers = databaseManager.fetchCustomers(id);
            dto.setCustomerList(customers);
        }
        catch(Exception er ){
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

    @GET
    @Path("customer")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerDTO getCustomer(@QueryParam("id") String id) {
        CustomerDTO dto = new CustomerDTO();

        try {
            Customer customer = databaseManager.getCustomer(id);
            dto.setCustomer( customer );

            customer.setOutstandingBill( databaseManager.fetchOutstandingBills( id ));
        }
        catch(Exception er ){
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

    @GET
    @Path("products")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductListDTO getProductList(@QueryParam("startswith") String id) {
        ProductListDTO dto = new ProductListDTO();
        try {
            List<Product> list = databaseManager.fetchProducts(id);
            dto.setProductList( list );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

    @GET
    @Path("billdetails")
    @Produces(MediaType.APPLICATION_JSON)
    public OrderDTO getBillDetails(@QueryParam("billId") String id) {
        OrderDTO dto = new OrderDTO();

        try {
            Order order = databaseManager.fetchOrder(Long.parseLong( id) );
            dto.setOrder(order);
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("collection")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerDTO addCollectionPayment(@FormParam("employeeId") String employeeId, @FormParam("billId") String billId, @FormParam("amount") String amount , @FormParam("customerId") String customerId)
    {
        CustomerDTO dto = new CustomerDTO();
        try {
           int rowCount =  databaseManager.updateCollectionPayment(Long.parseLong(employeeId), Long.parseLong(billId), Float.parseFloat(amount));
            if( rowCount == 0 ) {
                dto.setError(true);
                dto.setErrorMessage("Unable to update collection. Please try again!");
            }
            else{
                Customer customer  = databaseManager.getCustomer( customerId );
                customer.setOutstandingBill(  databaseManager.fetchOutstandingBills(customerId ));
                dto.setCustomer( customer );
            }
        }
        catch(Exception er){
            dto.setError( true );
            dto.setErrorMessage( er.toString());
            er.printStackTrace();
        }

        return  dto;
    }

    @POST
    @Path("saveOrder")
    @Produces(MediaType.APPLICATION_JSON)

    public CustomerDTO saveOrder(@RequestBody OrderDetailDTO orderDto) {

        System.out.println(orderDto );
        CustomerDTO dto = new CustomerDTO();

        try {
            //Map OrderDetailDTO to Business Object
            Order order = new Order();
            order.setCustomerId( Long.parseLong( orderDto.getCustomerId()) );
            order.setEmployeeId( Long.parseLong( orderDto.getEmployeeId()) );
            List<OrderEntry> orderEntries = new LinkedList<OrderEntry>();
            order.setOrderEntries( orderEntries );
            for (OrderEntryDTO entryDto:orderDto.getEntries()) {
                OrderEntry entry = new OrderEntry();
                entry.setQuantity(Integer.parseInt( entryDto.getQuantity()));
                entry.setProductId( Long.parseLong( entryDto.getProductId() ) );
                orderEntries.add(entry);
            }

            float paymentAmount = Float.parseFloat( orderDto.getPaymentAmount());
            databaseManager.saveOrder(order,paymentAmount);

            Customer customer  = databaseManager.getCustomer( orderDto.getCustomerId() );
            customer.setOutstandingBill(  databaseManager.fetchOutstandingBills(orderDto.getCustomerId() ));
            dto.setCustomer( customer );

            dto.setError( false );
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


}