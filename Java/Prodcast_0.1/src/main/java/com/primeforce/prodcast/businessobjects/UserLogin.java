package com.primeforce.prodcast.businessobjects;

/**
 * Created by Thiru on 11/1/2017.
 */
public class UserLogin
{
    private String mobileNumber,isdCode,country;
    private Long uId;
    private long pin;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }


    public Long getUId() {
        return uId;
    }

    public void setUId(Long uId) {
        this.uId = uId;
    }

    public Long getPin() {
        return pin;
    }

    public void setPin(Long pin) {
        this.pin = pin;
    }


    public String getCountryId() {
        return country;
    }

    public void setCountryId(String country) {
        this.country = country;
    }
}
