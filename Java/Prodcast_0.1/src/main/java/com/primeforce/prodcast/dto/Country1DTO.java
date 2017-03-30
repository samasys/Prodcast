package com.primeforce.prodcast.dto;

import com.primeforce.prodcast.businessobjects.Country1;


import java.util.List;

    /**
     * Created by Nandhini Govindasamy on 10/14/2016.
     */
    public class Country1DTO extends ProdcastDTO {
        private List<Country1> result;


        public List<Country1> getResult() {
            return result;
        }

        public void setResult(List<Country1> result) {
            this.result = result;
        }


    }


