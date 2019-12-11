package com.bplaz.merchant.Class;

public class AddProductClass {

    private String productID;
    private String productNAME;
    private String quantity;
    private String price;
    private String discount;
    private String total;


    public AddProductClass(String productID, String productNAME, String quantity, String price, String discount, String total){
        this.productID = productID;
        this.productNAME = productNAME;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.total = total;

    }

    public String getProductNAME() {
        return productNAME;
    }

    public String getProductID() {
        return productID;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductNAME(String productNAME) {
        this.productNAME = productNAME;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
