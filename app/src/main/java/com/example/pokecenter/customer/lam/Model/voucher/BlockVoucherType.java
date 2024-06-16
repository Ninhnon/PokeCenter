package com.example.pokecenter.customer.lam.Model.voucher;
import java.util.Date;
public class BlockVoucherType {
    String name;
    Date startDate;
    Date endDate;
    double value;

    public BlockVoucherType(
            String name,
            Date startDate,
            Date endDate,
            double value) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
