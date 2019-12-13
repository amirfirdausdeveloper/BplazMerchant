package com.bplaz.merchant.Class;

public class ProductListClass {

    private String productID;
    private String productNAME;
    private String productBRAND;
    private String productMANUFACTURE;
    private String serviceTYPE;
    private String sevice;
    private String available;
    private String imageURL;
    private String retail_price;
    private String base_price;


    public ProductListClass(String productID, String productNAME, String productBRAND, String productMANUFACTURE, String serviceTYPE, String sevice,
                            String available,String imageURL,String retail_price,String base_price){
        this.productID = productID;
        this.productNAME = productNAME;
        this.productBRAND = productBRAND;
        this.productMANUFACTURE = productMANUFACTURE;
        this.serviceTYPE = serviceTYPE;
        this.sevice = sevice;
        this.available = available;
        this.imageURL = imageURL;
        this.retail_price = retail_price;
        this.base_price = base_price;

    }

    public String getBase_price() {
        return base_price;
    }

    public String getRetail_price() {
        return retail_price;
    }


    public String getImageURL() {
        return imageURL;
    }

    public String getProductBRAND() {
        return productBRAND;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductMANUFACTURE() {
        return productMANUFACTURE;
    }

    public String getProductNAME() {
        return productNAME;
    }

    public String getServiceTYPE() {
        return serviceTYPE;
    }

    public String getSevice() {
        return sevice;
    }

    public String getAvailable() {
        return available;
    }
}
