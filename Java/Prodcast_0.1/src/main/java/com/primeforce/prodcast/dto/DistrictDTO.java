package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.District;

import java.util.List;
/**
 * Created by Thiru on 29/1/2017.
 */
public class DistrictDTO extends ProdcastDTO
{
    private List<District> result;


    public List<District> getResult() {
        return result;
    }

    public void setResult(List<District> result) {
        this.result = result;
    }

}
