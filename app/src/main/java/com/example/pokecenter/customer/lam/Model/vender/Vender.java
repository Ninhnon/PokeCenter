package com.example.pokecenter.customer.lam.Model.vender;

import com.example.pokecenter.customer.lam.Model.product.Product;

import java.util.Date;
import java.util.List;

public class Vender {

    private String shopName;

    private String avatar;

    private String address;

    private String phoneNumber;

    private int followCount;

    private String registrationDate;

    private int totalProduct;

    public Vender() {

    }

    public Vender(String shopName, String avatar, String address, String phoneNumber, int followCount, String registrationDate, int totalProduct) {
        this.shopName = shopName;
        this.avatar = avatar;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.followCount = followCount;
        this.registrationDate = registrationDate;
        this.totalProduct = totalProduct;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registerDay) {
        this.registrationDate = registerDay;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
    }
}
