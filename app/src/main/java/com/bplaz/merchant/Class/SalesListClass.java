package com.bplaz.merchant.Class;

public class SalesListClass {

    private String id;
    private String status_job;
    private String assign_date;
    private String name_customer;
    private String geo_location_customer;
    private String product_name;



    public SalesListClass(String id,String status_job,String assign_date,String name_customer,String geo_location_customer,String product_name){
        this.id = id;
        this.status_job = status_job;
        this.assign_date = assign_date;
        this.name_customer = name_customer;
        this.geo_location_customer = geo_location_customer;
        this.product_name = product_name;

    }

    public String getAssign_date() {
        return assign_date;
    }

    public String getId() {
        return id;
    }

    public String getName_customer() {
        return name_customer;
    }

    public String getStatus_job() {
        return status_job;
    }

    public String getGeo_location_customer() {
        return geo_location_customer;
    }

    public String getProduct_name() {
        return product_name;
    }

}
