package com.primeforce.prodcast;

/**
 * Created by Thiru on 7/1/2017.
 */


import com.primeforce.prodcast.businessobjects.UserRegistration;
import com.primeforce.prodcast.dao.DbManager;
import com.primeforce.prodcast.dto.*;
import com.primeforce.prodcast.util.Amazon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Random;



/**
 * Created by thiru on 01/11/2016.
 */
@Named
@Path("/user/")

public class UserRest {
    private final DbManager dbManager;

    @Autowired
    public UserRest(DbManager manager)

    {
        dbManager = manager;
    }

    @GET
    @Path("getCountries")
    @Produces(MediaType.APPLICATION_JSON)
    public Country1DTO getCountries() {
        Country1DTO dto = new Country1DTO();

        try {
            List countries = dbManager.fetchCountries();
            dto.setResult(countries);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }

    @GET
    @Path("getStates")
    @Produces(MediaType.APPLICATION_JSON)
    public StateDTO getState() {
        StateDTO dto = new StateDTO();

        try {
            List state = dbManager.fetchStates();
            dto.setResult(state);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }
    @GET
    @Path("getDistricts")
    @Produces(MediaType.APPLICATION_JSON)
    public DistrictDTO getDistrict() {
        DistrictDTO dto = new DistrictDTO();

        try {
            List district = dbManager.fetchDistricts();
            dto.setResult(district);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }
    @GET
    @Path("getTaluks")
    @Produces(MediaType.APPLICATION_JSON)
    public TalukDTO getTaluk() {
        TalukDTO dto = new TalukDTO();

        try {
            List taluk = dbManager.fetchTaluks();
            dto.setResult(taluk);
        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }

    @POST
    @Path("registration")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO userRegistration(@FormParam("name") String name,
                                     @FormParam("gender") int gender,
                                     @FormParam("dob") String dob,
                                     @FormParam("fatherName") String fatherName,
                                     @FormParam("motherName") String motherName,
                                     @FormParam("race") String race,
                                     @FormParam("education") String education,
                                     @FormParam("occupation") String occupation,
                                     @FormParam("doorNumber") String doorname,
                                     @FormParam("street") String street,
                                     @FormParam("town") String town,
                                     @FormParam("district") String district,
                                     @FormParam("taluk") String taluk,
                                     @FormParam("state") String state,
                                     @FormParam("country") String country,
                                     @FormParam("mobilePhone") String mobilePhone,
                                     @FormParam("email") String email,
                                     @FormParam("aadhar") String aadhar,
                                     @FormParam("bloodGroup") String bloodGroup,
                                     @FormParam("registerId") String registerId)

    {
        AdminDTO dto = new AdminDTO();

        try {


            Random random = new Random();
            int code = (1000 + random.nextInt(9000));
            System.out.println(code);
            int regist;
            if (registerId == null) {
                List userData = dbManager.fetchMobileNumber1(country, mobilePhone);


                if (userData.size() > 0)

                {
                    dto.setError(true);
                    dto.setErrorMessage("The Mobile number Already Exists");
                    return dto;
                }

                regist = dbManager.registerUser(name, gender, dob, fatherName, motherName, race, education,
                        occupation, doorname, street, town, district,taluk, state,
                        country, mobilePhone, email, aadhar, bloodGroup, code);
                String phoneNumber = dbManager.fetchCustomerCountryId1(country) + mobilePhone;

                String subject = "Your Pin Number is:" + code + "\n";
                Amazon.sendSMS(subject, phoneNumber);
            } else {

                regist = dbManager.updateCustomer1(name, gender, dob, fatherName, motherName, race, education,
                        occupation, doorname, street, town, district,taluk, state,
                        country, mobilePhone, email, aadhar, bloodGroup, Long.parseLong(registerId));
            }

            if (regist == 0) {
                dto.setError(true);
            } else {
                dto.setResult(regist);
            }

            dto.setSuccessMessage("Successfully Registered");


        } catch (Exception er)

        {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());

        }
        return dto;
    }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerLoginDTO authenticate(@FormParam("mobilePhone") String mobilePhone, @FormParam("country") String country, @FormParam("pin") int pin)
    {

        CustomerLoginDTO dto = new CustomerLoginDTO();


        try {

            UserRegistration confirmation = dbManager.loginCustomer1(mobilePhone, country, pin);
            System.out.println("UId=" + confirmation.getuId());
            dto.setResult(confirmation);



        } catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }

    @POST
    @Path("retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO authenticate(@FormParam("mobilePhone") String mobilePhone, @FormParam("country") String country)throws Exception {
        AdminDTO dto = new AdminDTO();
        try {
            if(mobilePhone!=null) {
                int  pin = dbManager.getPinFromMobile1(mobilePhone);

                if (pin!=-1)
                {
                    String phoneNumber = dbManager.fetchCustomerCountryId1(country) + mobilePhone;

                    String subject = "Your Pin Number is " + pin;
                    System.out.println(pin);

                    Amazon.sendSMS(subject,phoneNumber);
                     return dto;
                }

                else
                    {
                    dto.setError(true);
                    dto.setErrorMessage("The mobile number is not registered with URAVUPAALAM");

                }
            }
        }
        catch (Exception er) {
            er.printStackTrace();
            dto.setError(true);
            dto.setErrorMessage(er.toString());
        }
        return dto;
    }
    @GET
    @Path("getRegistrationDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Registration1DTO getRegistrationDetails(){

        Registration1DTO dto = new Registration1DTO();
        List<UserRegistration> registration=null;
        try{
            registration=dbManager.getAllRegistors1();
            dto.setResult(registration);
        }
        catch(Exception er ){
            dto.setError( true );
            dto.setErrorMessage( er.toString() );
            er.printStackTrace();
        }
        return dto;
    }

    }


