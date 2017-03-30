package com.primeforce.prodcast.dto;
import com.primeforce.prodcast.businessobjects.State;

import java.util.List;
/**
 * Created by Thiru on 29/1/2017.
 */
public class StateDTO extends ProdcastDTO
{
    private List<State> result;


    public List<State> getResult() {
        return result;
    }

    public void setResult(List<State> result) {
        this.result = result;
    }

}
