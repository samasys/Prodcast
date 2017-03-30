package com.primeforce.prodcast.dto;

/**
 * Created by Thiru on 11/1/2017.
 */

    public class UserLoginDTO<T> extends ProdcastDTO
{
        private boolean verified;
        private T result;

        public T getResult(){
            return result;
        }

        public void setResult(T result){
            this.result = result;
        }

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }


    }

