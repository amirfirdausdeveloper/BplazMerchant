package com.bplaz.merchant.Class;

public class ProductClass {

    private String price;
    private String name;
    private String type;


    public ProductClass(String price, String name, String type){
        this.price = price;
        this.name = name;
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
