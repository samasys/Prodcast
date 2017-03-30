package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.Taluk;

import java.util.List;
/**
 * Created by Thiru on 29/1/2017.
 */
public class TalukDTO extends ProdcastDTO

{
    private List<Taluk> result;


    public List<Taluk> getResult() {
        return result;
    }

    public void setResult(List<Taluk> result) {
        this.result = result;
    }

}
