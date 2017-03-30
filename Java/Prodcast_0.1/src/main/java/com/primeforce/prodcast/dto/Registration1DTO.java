package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.UserRegistration;

import java.util.List;

/**
 * Created by Thiru on 12/1/2017.
 */
public class Registration1DTO extends ProdcastDTO
{

    private List<UserRegistration> result;

    public List<UserRegistration> getResult() {
        return result;
    }

    public void setResult(List<UserRegistration> result) {
        this.result = result;
    }
}
