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


    public ProductListClass(String productID, String productNAME, String productBRAND, String productMANUFACTURE, String serviceTYPE, String sevice,
                            String available,String imageURL){
        this.productID = productID;
        this.productNAME = productNAME;
        this.productBRAND = productBRAND;
        this.productMANUFACTURE = productMANUFACTURE;
        this.serviceTYPE = serviceTYPE;
        this.sevice = sevice;
        this.available = available;
        this.imageURL = imageURL;

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
