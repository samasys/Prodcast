package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.UserRegistration;

import java.util.List;
/**
 * Created by Thiru on 28/1/2017.
 */
public class UserListDTO extends ProdcastDTO
{
    public List<UserRegistration> getCustomerList()
    {
        return customerList;
    }

    public void setCustomerList(List<UserRegistration> customerList)
    {
        this.customerList = customerList;
    }

    private List<UserRegistration> customerList;

}
