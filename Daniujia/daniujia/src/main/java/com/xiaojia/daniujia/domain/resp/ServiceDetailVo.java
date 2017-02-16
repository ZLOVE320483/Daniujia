package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ServiceDetailVo {

    private int applyStep;
    private List<ServiceproductsEntity> serviceproducts;

    public int getApplyStep() {
        return applyStep;
    }

    public void setApplyStep(int applyStep) {
        this.applyStep = applyStep;
    }

    public void setServiceproducts(List<ServiceproductsEntity> serviceproducts) {
        this.serviceproducts = serviceproducts;
    }

    public List<ServiceproductsEntity> getServiceproducts() {
        return serviceproducts;
    }

    public static class ServiceproductsEntity {
        private int id;
        private int servicetype;
        private int status;
        private String price;
        private String applyPrice;

        public String getApplyPrice() {
            return applyPrice;
        }

        public void setApplyPrice(String applyPrice) {
            this.applyPrice = applyPrice;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setServicetype(int servicetype) {
            this.servicetype = servicetype;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public int getServicetype() {
            return servicetype;
        }

        public int getStatus() {
            return status;
        }

        public String getPrice() {
            return price;
        }
    }
}
