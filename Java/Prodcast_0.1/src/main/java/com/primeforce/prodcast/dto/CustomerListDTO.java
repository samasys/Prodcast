package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.Bill;
import com.primeforce.prodcast.businessobjects.Customer;

import java.util.List;

/**
 * Created by sarathan732 on 4/23/2016.
 */
public class CustomerListDTO extends ProdcastDTO {

    public List<Customer> getCustomerList()
    {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList)
    {
        this.customerList = customerList;
    }

    private List<Customer> customerList;
    private List<Bill> outstandingBills;

    public List<Bill> getOutstandingBills() {
        return outstandingBills;
    }

    public void setOutstandingBills(List<Bill> outstandingBills) {
        this.outstandingBills = outstandingBills;
    }
}
