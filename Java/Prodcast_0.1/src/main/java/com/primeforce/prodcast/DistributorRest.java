package com.primeforce.prodcast;

import com.primeforce.prodcast.businessobjects.*;
import com.primeforce.prodcast.businessobjects.Collection;
import com.primeforce.prodcast.dao.CompanySetting;
import com.primeforce.prodcast.dao.DatabaseManager;
import com.primeforce.prodcast.dao.Distributor;
import com.primeforce.prodcast.dao.DistributorDBSql;
import com.primeforce.prodcast.dto.*;
import com.primeforce.prodcast.messaging.MessagingManager;
import com.primeforce.prodcast.messaging.OrderDataProvider;
import com.primeforce.prodcast.util.Amazon;
import com.primeforce.prodcast.util.Notifier;
import com.primeforce.prodcast.util.TimeZoneConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Named;
import javax.print.attribute.IntegerSyntax;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sarathan732 on 4/22/2016.
 */
@Named
@Path("/distributor/")
public class DistributorRest {


    private final DatabaseManager databaseManager;

    @Autowired
    public DistributorRest(DatabaseManager manager) {
        databaseManager = manager;
    }

    @GET
    @Path("getEmployees")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Employee>> getEmployees(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<Employee>> dto = new AdminDTO<List<Employee>>();

        try {
            List<Employee> employees = databaseManager.fetchEmployeesForDistributor(Long.parseLong(employeeId ));
            dto.setResult( employees );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

    @GET
    @Path("getBrands")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Brand>> getBrands(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<Brand>> dto = new AdminDTO<List<Brand>>();

        try {
            List<Brand> brands = databaseManager.fetchBrandsForDistributor(Long.parseLong(employeeId ));
            dto.setResult( brands);
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("returnProduct")
    @Produces(MediaType.APPLICATION_JSON)

    public OrderDTO returnProduct(@FormParam("employeeId") String employeeId,
                                   @FormParam("billNo") String billNumber,
                                   @FormParam("quantity") int quantity ,
                                   @FormParam("comments ") String comments,
                                   @FormParam("productId") long productId)
    {

        OrderDTO dto = new OrderDTO();


        try {
            int quantityValue=0;
            Order order = databaseManager.fetchOrder( Long.parseLong(billNumber),Long.parseLong(employeeId));

            int rowCount=0;
           // int rowCount = databaseManager.returnProduct(Long.parseLong(employeeId), Long.parseLong(billNumber),quantity,comments,productId);
            List<OrderEntry> orderEntries = order.getOrderEntries();
            //order.setOrderEntries( orderEntries );
            OrderEntry entry;
            for(int i=0;i<orderEntries.size();i++) {
                entry = orderEntries.get(i);
                if (entry.getProductId() == productId) {
                    quantityValue = entry.getQuantity();
                    
                }
            }

            orderEntries = order.getReturnEntries();
            //order.setOrderEntries( orderEntries );
            for(int i=0;i<orderEntries.size();i++) {
                entry = orderEntries.get(i);
                if (entry.getProductId() == productId) {
                    quantityValue -=entry.getQuantity();

                }
            }
            quantityValue -=quantity;

            if(quantityValue<0)
            {
                dto.setError(true);
                dto.setErrorMessage("The return quantity cannot be greater than the Existing Order Quantity");
                return dto;
            }

                rowCount = databaseManager.returnProduct(order,Long.parseLong(billNumber),productId, quantity,comments,Long.parseLong(employeeId));

            if (rowCount!=1) {
                dto.setError(true);
                dto.setErrorMessage("Unable to return order");
                return dto;
            }

           // order = databaseManager.fetchOrder( Long.parseLong(billNumber),Long.parseLong(employeeId));
            try {
                OrderDataProvider orderDataProvider = new OrderDataProvider();
                orderDataProvider.setBillNo(Long.parseLong(billNumber));
                orderDataProvider.setEmployeeId(Long.parseLong(employeeId));
                orderDataProvider.setType(2);
                //orderDataProvider.setAmountPaid(Float.parseFloat(amount));
                MessagingManager msgManager = new MessagingManager();
                String mailMessage = msgManager.mailMerge(0, orderDataProvider,databaseManager);
                String subject = orderDataProvider.getSubject();
                order = orderDataProvider.getOrder();
                //Amazon.sendSMS(subject, orderDataProvider.getSmsPhoneNumber());
                String[] emailds = { order.getCustomerEmail() , order.getEmployeeEmail(), order.getDistributorEmail()  };
                Notifier.sendNotification( orderDataProvider.getSmsPhoneNumber() , subject , mailMessage , emailds );
            }
            catch(Exception er){
                er.printStackTrace();
            }




            dto.setOrder( order );

        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }


    @POST
    @Path("saveBrand")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Brand>> saveBrand(@FormParam("employeeId") String employeeId,
                                           @FormParam("brandId") String brandId,
                                           @FormParam("brandName") String brandName)
    {
        AdminDTO<List<Brand>> dto = new AdminDTO<List<Brand>>();
        try {
            if (brandId == null || brandId.trim().length() == 0)
            {
                List<Brand> brands = databaseManager.saveBrandForDistributor(Long.parseLong(employeeId), brandName);
                if (brands == null) {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save brand");
                } else
                    dto.setResult(brands);
            }
                else {
                int rowCount = databaseManager.updateAbcsDistributor(1, brandName, Long.parseLong(employeeId), Long.parseLong(brandId));
                if (rowCount != 1)
                {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save brand");

                }

                }
            }

        catch (Exception er) {
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }

    @GET
    @Path("setting")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<CompanySetting> getSetting(@QueryParam("employeeId") String employeeId) {

        AdminDTO dto = new AdminDTO();
        try {
            dto.setResult( databaseManager.fetchCompanySetting(Long.parseLong( employeeId) ));
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString() );
        }
        return dto;

    }


    @POST
    @Path("saveSettings")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<CompanySetting> saveSettings(
                                                 @FormParam("set_tax") String tax,
                                                 @FormParam("employeeId") String employeeId,
                                                 @FormParam("set_comp") String companyname,
                                                 @FormParam("set_addr") String address,
                                                 @FormParam("set_city") String city ,
                                                 @FormParam("set_state") String state,
                                                 @FormParam("set_postal") String postal ,
                                                 @FormParam("set_country") String country,
                                                 @FormParam("set_ph") String phonenumber,
                                                 @FormParam("set_fax") String fax,
                                                 @FormParam("set_timezone") String timezone)
    {
        AdminDTO<CompanySetting> dto = new AdminDTO<CompanySetting>();
        try {
            long distributorId = databaseManager.getDistributorForEmployee( Long.parseLong ( employeeId  ) );
            int rowCount = databaseManager.updateSettings( distributorId,Float.parseFloat(tax) , companyname, address, city, state, postal, country,phonenumber,fax,timezone, Long.parseLong( employeeId));

            if( rowCount == 0 ) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save settings.Please try again!");
            }
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }


    @GET
    @Path("getAreas")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Area>> getAreas(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<Area>> dto = new AdminDTO<List<Area>>();

        try {
            List<Area> areas = databaseManager.fetchAreasForDistributor(Long.parseLong(employeeId ));
            dto.setResult( areas );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("saveArea")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Area>> saveArea(@FormParam("employeeId") String employeeId,
                                         @FormParam("areaId") String areaId,
                                         @FormParam("areaName") String areaName)
    {
        AdminDTO<List<Area>> dto = new AdminDTO<List<Area>>();
        try {
            List<Area> areas;

            if (areaId == null || areaId.trim().length() == 0)
            {

                areas = databaseManager.saveAreaForDistributor(Long.parseLong(employeeId), areaName);
                if (areas == null) {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save area");
                }
                else
                    dto.setResult(areas);

            }
            else {
                int rowCount = databaseManager.updateAbcsDistributor(0, areaName, Long.parseLong(employeeId), Long.parseLong(areaId));
                if (rowCount != 1) {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save area");

                }
            }


        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }

    @GET
    @Path("getCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Category>> getCategories(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<Category>> dto = new AdminDTO<List<Category>>();

        try {
            List<Category> categories = databaseManager.fetchCategoriesForDistributor(Long.parseLong(employeeId ));
            dto.setResult( categories );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("deleteOrder")
    @Produces(MediaType.APPLICATION_JSON)

    public AdminDTO deleteOrder(@FormParam("billNo") String billNo,@FormParam("customerId") String customerId)
    {
        AdminDTO dto = new AdminDTO();


        try {


            int rowCount = databaseManager.deleteOrder( Long.parseLong(billNo) );



            if (rowCount!=1) {
                dto.setError(true);
                dto.setErrorMessage("Unable to delete order");
            }
            else
            {
                dto.setResult(databaseManager.fetchOutstandingBills(customerId));

            }




        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }



    @POST
    @Path("updateOrderStatus")
    @Produces(MediaType.APPLICATION_JSON)

    public AdminDTO updateOrderStatus(@FormParam("billNo") String billNo,
                                      @FormParam("orderStatus") String orderStatus,
                                      @FormParam("customerId") String customerId ,
                                      @FormParam("employeeId") String employeeId ) {

        AdminDTO dto = new AdminDTO();


        try {


            int rowCount = databaseManager.updateOrderStatus(Long.parseLong(billNo), orderStatus);



            if (rowCount != 1) {
                dto.setError(true);
                dto.setErrorMessage("Unable to Update order Status");
            } else {

                dto.setResult(databaseManager.fetchOutstandingBills(customerId));

            }
               try
               {
                   Order order = new Order();
                   float paymentAmount = order.getTotalAmount();
                   OrderDataProvider orderDataProvider = new OrderDataProvider();
                   orderDataProvider.setBillNo(Long.parseLong(billNo));
                   orderDataProvider.setEmployeeId(Long.parseLong( employeeId) );
                   orderDataProvider.setAmountPaid(paymentAmount);
                   orderDataProvider.setType( 3 );
                   MessagingManager msgManager = new MessagingManager();
                   String mailMessage = msgManager.mailMerge(0, orderDataProvider,databaseManager);
                   //String subject = "Order #" + billNo + " is fullfilled  by " + orderDataProvider.get + "\n";
                   order = orderDataProvider.getOrder();
                   String[] emailds = { order.getCustomerEmail() , order.getEmployeeEmail(), order.getDistributorEmail()  };
                   Notifier.sendNotification( orderDataProvider.getSmsPhoneNumber() , orderDataProvider.getSubject() , mailMessage , emailds );



         }


         catch (Exception er) {
            er.printStackTrace();
           }
        }
            catch (Exception er) {
                er.printStackTrace();
                dto.setError(true);
                dto.setErrorMessage(er.toString());
            }


            return dto;


    }


    @POST
    @Path("saveCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Category>> saveCategory(@FormParam("employeeId") String employeeId,
                                                 @FormParam("catId") String catId,
                                                 @FormParam("categoryName") String categoryName)
    {
        AdminDTO<List<Category>> dto = new AdminDTO<List<Category>>();
        try
        {
        if (catId == null || catId.trim().length() == 0) {
            List<Category> category = databaseManager.saveCategoryForDistributor(Long.parseLong(employeeId), categoryName);
            if (category == null) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save category");
            }
            else
                dto.setResult(category);
        } else {
            int rowCount = databaseManager.updateAbcsDistributor(2, categoryName, Long.parseLong(employeeId), Long.parseLong(catId));
            if (rowCount != 1) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save category");

            }
        }
    }
        catch(Exception er)

    {
        er.printStackTrace();
        dto.setError(true);
        dto.setErrorMessage(er.toString());
    }

            return dto;
    }


    @GET
    @Path("getSubCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<SubCategory>> getSubCategories(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<SubCategory>> dto = new AdminDTO<List<SubCategory>>();

        try {
            List<SubCategory> categories = databaseManager.fetchSubCategoriesForDistributor(Long.parseLong(employeeId ));
            dto.setResult( categories );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("saveSubCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<SubCategory>> saveSubcategories(@FormParam("employeeId") String employeeId,
                                                        @FormParam("subCategoryId") String subCategoryId,
                                                         @FormParam("subCategoryName") String subCategoryName,
                                                         @FormParam("categoryId") String categoryId)
    {
        AdminDTO<List<SubCategory>> dto = new AdminDTO<List<SubCategory>>();
        try {
            if(subCategoryId==null || subCategoryId.trim().length()==0)
            {
                List<SubCategory> subcategory = databaseManager.saveSubCategoryForDistributor(Long.parseLong(employeeId), subCategoryName, Long.parseLong(categoryId));
                if (subcategory == null) {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save subcategory");
                } else
                    dto.setResult(subcategory);
            }
        else
            {
                int rowCount= databaseManager.updateAbcsDistributor(3,subCategoryName ,Long.parseLong(employeeId),  Long.parseLong( subCategoryId ));
                if (rowCount != 1)
                {
                    dto.setError(true);
                    dto.setErrorMessage("Unable to save subcategory");

                }

            }
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }

    @GET
    @Path("getProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Product>> getProducts(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<Product>> dto = new AdminDTO<List<Product>>();

        try {
            List<Product> products = databaseManager.fetchProductsForDistributor(Long.parseLong(employeeId ));
            dto.setResult( products );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


    @POST
    @Path("saveProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Product>> saveProduct(@FormParam("employeeId") String employeeId, @FormParam("productId") String productId, @FormParam("productName") String productName, @FormParam("productDesc") String productDesc, @FormParam("productSku") String productSku, @FormParam("unitPrice") String unitPrice,@FormParam("priceType") String priceType,@FormParam("categoryId") String categoryId, @FormParam("subCategoryId") String subCategoryId, @FormParam("brandId") String brandId, @FormParam("active") String active ,@FormParam("salesTax") String salesTaxRate, @FormParam("otherTax") String otherTaxRate,@FormParam("retailPrice") String retailPrice, @FormParam("uom") String unitofMeasure  ){
        AdminDTO<List<Product>> dto = new AdminDTO<List<Product>>();
        try {
            List<Product> result;
            System.out.println("Product>>"+productId+"<<");
            if(productId==null || productId.trim().length()==0 || productId.equals("0"))
            {
                System.out.println("Saving Product");
                result=databaseManager.saveProductForDistributor(Long.parseLong(employeeId), 0 , productName , productDesc , productSku , Float.parseFloat(unitPrice) , priceType,Long.parseLong(categoryId ) , Long.parseLong(subCategoryId ) , Long.parseLong(brandId ), Boolean.parseBoolean( active ) , salesTaxRate, otherTaxRate,retailPrice,unitofMeasure );
            }
            else
            {
                System.out.println("Updating Product");
                result = databaseManager.updateProductForDistributor(Long.parseLong(employeeId), Long.parseLong( productId ) , productName , productDesc , productSku , Float.parseFloat(unitPrice) , priceType,Long.parseLong(categoryId ) , Long.parseLong(subCategoryId ) , Long.parseLong(brandId ), Boolean.parseBoolean( active ),salesTaxRate, otherTaxRate,retailPrice,unitofMeasure );
            }

            if( result == null ) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save product");
            }
            else
                dto.setResult( result );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }

    @POST
    @Path("saveEmployee")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<Employee>> saveEmployee(@FormParam("employeeId") String employeeId,
                                                 @FormParam("newEmployeeId") String newEmployeeId,
                                                 @FormParam("firstName") String firstName,
                                                 @FormParam("lastName") String lastName,
                                                 @FormParam("title") String title,
                                                 @FormParam("sex") String sex,
                                                 @FormParam("dateOfBirth") String dateOfBirth,
                                                 @FormParam("salary") String salary,
                                                 @FormParam("hireDate") String hireDate,
                                                 @FormParam("terminationDate") String terminationDate,
                                                 @FormParam("allowance") String allowance,
                                                 @FormParam("userRole") String userRole,
                                                 @FormParam("address1") String address1,
                                                 @FormParam("address2") String address2,
                                                 @FormParam("address3") String address3,
                                                 @FormParam("city") String city,
                                                 @FormParam("state") String state,

                                                 @FormParam("countryId") String countryId,
                                                 @FormParam("postalCode") String postalCode,
                                                 @FormParam("location") String location,
                                                 @FormParam("cellPhone") String cellPhone,
                                                 @FormParam("homePhone") String homePhone,
                                                 @FormParam("workPhone") String workPhone,
                                                 @FormParam("emailId") String emailId,
                                                 @FormParam("active") String active,
                                                 @FormParam("comments") String comments,
                                                 @FormParam("areaIds") String areas
                                                 ){
        AdminDTO<List<Employee>> dto = new AdminDTO<List<Employee>>();
        try {
            if( newEmployeeId == null || newEmployeeId.trim().equals("")) newEmployeeId = "0";
            if( terminationDate== null || terminationDate.trim().equals("null")|| terminationDate.trim().length()== 0 ) terminationDate = null;
            Long employeeForEmail = databaseManager.fetchEmailInUse(emailId);
            if( employeeForEmail != null )
            {
                if(!employeeForEmail.equals( Long.parseLong(newEmployeeId))) {
                    dto.setErrorMessage("The email address is already in use. Please use a different email address");
                    dto.setError(true);
                    return dto;
                }
            }

            List<Employee> result ;
            if(newEmployeeId.equals("0")) {
                result = databaseManager.saveEmployeeForDistributor(Long.parseLong(employeeId),
                        firstName, lastName, title, sex, dateOfBirth, salary, hireDate, terminationDate, allowance,
                        userRole, address1, address2, address3, city, state, countryId, postalCode,
                        location, cellPhone, homePhone, workPhone, emailId, active, comments, areas, Long.parseLong(newEmployeeId), -1);
            }
            else {
                result = databaseManager.updateEmployeeForDistributor(Long.parseLong(employeeId),
                        firstName, lastName, title, sex, dateOfBirth, salary, hireDate, terminationDate, allowance,
                        userRole, address1, address2, address3, city, state, countryId, postalCode,
                        location, cellPhone, homePhone, workPhone, emailId, active, comments, areas, Long.parseLong(newEmployeeId), -1);
            }
            if( result == null ) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save Employee");
            }
            else
                dto.setResult( result );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }

        return dto;
    }

    @POST
    @Path("saveExpense")
    @Produces(MediaType.APPLICATION_JSON)
    public ExpenseDTO saveExpense(@FormParam("expenseId") String expenseId,@FormParam("expenseDate") String expenseDate, @FormParam("account") String account, @FormParam("catgId") int categoryId,
                                   @FormParam("desc1") String desc1, @FormParam("desc2") String desc2,
                                   @FormParam("amount") double amount, @FormParam("payMode") String paymode, @FormParam("employeeId") String userId) {
        ExpenseDTO dto = new ExpenseDTO();
        try {
            int rowCount;
            System.out.println("ExpenseId==>"+expenseId+"<<");
			if(expenseId==null || expenseId.trim().length()==0)
            {
                rowCount  = databaseManager.saveExpense(0, expenseDate, account, categoryId, desc1, desc2, amount, paymode, userId);
            }
            else
            {
                rowCount = databaseManager.updateExpense(0,Long.parseLong(expenseId),account, categoryId, desc1, desc2, amount, paymode, userId,expenseDate);

            }
           
            if (rowCount == 0) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save expense. Please try again!");
            }
            List<Expense> expenses = databaseManager.fetchExpenseForDistributor( new java.sql.Date(System.currentTimeMillis())  , userId );

            dto.setExpenses( expenses );

        } catch (Exception er) {
            dto.setError(true);
            dto.setErrorMessage(er.toString());
            er.printStackTrace();
        }
        return dto;
    }

    @GET
    @Path("fetchExpense")
    @Produces(MediaType.APPLICATION_JSON)
    public ExpenseDTO fetchExpenses(@QueryParam("employeeId") String userId) {
        ExpenseDTO dto = new ExpenseDTO();
        try {

            List<Expense> expenses = databaseManager.fetchExpenseForDistributor(new java.sql.Date(System.currentTimeMillis()) , userId );
            dto.setExpenses( expenses );

        } catch (Exception er) {
            dto.setError(true);
            dto.setErrorMessage(er.toString());
            er.printStackTrace();
        }
        return dto;
    }

    @GET
    @Path("getReportType")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO getReportType() {
        AdminDTO dto = new AdminDTO();

        try {
            List reportType = databaseManager.fetchReportType();
            dto.setResult( reportType );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }

@GET
@Path("report")
@Produces(MediaType.APPLICATION_JSON)
public DistributorReportDTO getReport(@QueryParam("reportType") String reportType, @QueryParam("employeeId") long employeeId,@QueryParam("startDate") String customStartDate , @QueryParam("endDate") String customEndDate ,@QueryParam("distEmployee") long selectedEmployee,@QueryParam("reportId") long reportId) {
    DistributorReportDTO dto = new DistributorReportDTO();
        final long DAY = 24*60*60*1000;
        try {

            Calendar cal = Calendar.getInstance();
            java.sql.Date startDate = java.sql.Date.valueOf("2016-04-20");
            java.sql.Date endDate =new java.sql.Date(Calendar.getInstance().getTime().getTime());

            if( reportType.equals("today")){
                startDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                endDate = startDate;
            }
            else if( reportType.equals("yesterday")){
                startDate = new java.sql.Date(Calendar.getInstance().getTime().getTime() -DAY );
                endDate = startDate;
            }
            else if ( reportType.equals("week")){
                endDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                cal.set(Calendar.DAY_OF_WEEK , Calendar.MONDAY );
                startDate = new java.sql.Date(cal.getTime().getTime());
            }
            else if ( reportType.equals("last7")){
                endDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                startDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() - 7*DAY );
            }
            else if ( reportType.equals("month")){
                endDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                cal.set(Calendar.DAY_OF_MONTH , 1 );
                startDate = new java.sql.Date(cal.getTime().getTime());
            }

            else if ( reportType.equals("last30")){
                endDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                startDate = new java.sql.Date( Calendar.getInstance().getTime().getTime() - 30*DAY );
            }
            else{
                startDate = new java.sql.Date( new SimpleDateFormat(DatabaseManager.DATE_FORMAT).parse(customStartDate).getTime());
                endDate = new java.sql.Date( new SimpleDateFormat(DatabaseManager.DATE_FORMAT).parse(customEndDate).getTime());

            }
            List<ReportType> reportSql=databaseManager.fetchReportType();

            for(int i=0;i<reportSql.size();i++) {
                ReportType rt = reportSql.get(i);
                if (rt.getReportId() == reportId) {
                    dto.setReportName(rt.getReportName());
                    dto.setHeader(rt.getHeader());
                    dto.setAttributes(rt.getAttributes());
                    dto.setExportHeader(rt.getExportHeader());
                    dto.setExportAttributes(rt.getExportAttributes());
                    Object obj=databaseManager.fetchReportForDistributors(rt.getReportSql(),rt.getReportMapper(),startDate,endDate,employeeId,rt.getCondition(),rt.getGroupedBy(),selectedEmployee);
                    dto.setResult(obj);
                    break;
                }



            }

}
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }




    @POST
    @Path("saveExpenseCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<ExpenseCategory>> saveExpenseCategory(@FormParam("catgDesc") String catgDesc,
                                           @FormParam("employeeId") String userId,
                                                               @FormParam("expenseCategoryId") String expenseCategoryId) {
        AdminDTO<List<ExpenseCategory>> dto = new AdminDTO<List<ExpenseCategory>>();
        try {
            int rowCount;
			if(expenseCategoryId==null) {
                rowCount = databaseManager.saveExpenseCategory(catgDesc, userId);
            }
            else
            {
                rowCount = databaseManager.updateExpenseCategory(Long.parseLong(expenseCategoryId),catgDesc, userId);
            }
            
            if (rowCount == 0) {
                dto.setError(true);
                dto.setErrorMessage("Unable to save expense. Please try again!");
            }
            List<ExpenseCategory> products = databaseManager.getExpenseCategories(Long.parseLong(userId ));
            dto.setResult( products );
        } catch (Exception er) {
            dto.setError(true);
            dto.setErrorMessage(er.toString());
            er.printStackTrace();
        }
        return dto;
    }
    @GET
    @Path("getExpenseCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<List<ExpenseCategory>> getExpenseCategories(@QueryParam("employeeId") String employeeId) {
        AdminDTO<List<ExpenseCategory>> dto = new AdminDTO<List<ExpenseCategory>>();

        try {
            List<ExpenseCategory> products = databaseManager.getExpenseCategories(Long.parseLong(employeeId ));
            dto.setResult( products );
        }
        catch(Exception er){
            er.printStackTrace();
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
        }
        return dto;
    }


}