package com.primeforce.prodcast.dto;

import java.util.List;

/**
 * Created by sarathan732 on 4/26/2016.
 */
public class OrderDetailDTO extends ProdcastDTO{
    private String customerId;
    private String employeeId;
    private String paymentAmount;
    private String discountValue;
    private String discountType;
    private List<OrderEntryDTO> entries;

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<OrderEntryDTO> entries) {
        this.entries = entries;
    }



    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


}
