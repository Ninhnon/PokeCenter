package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.customer.lam.Model.order.DetailOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;

public class Order {
    private String id;
    private int totalAmount;
    private Date createDateTime;
    private List<DetailOrder> ordersDetail;
    private Date deliveryDate;
    private boolean isExpand;
    private String customerName;
    private String customerPhoneNumber;
    private String deliveryAddress;
    private String urlDb = "https://pokecenter-ae954-default-rtdb.firebaseio.com/";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private OrderState state;
    public Order(String id, int totalAmount, Date createDateTime, List<DetailOrder> ordersDetail, OrderState state) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.createDateTime = createDateTime;
        this.ordersDetail = ordersDetail;
        this.state = state;
        this.state.setOrder(this); 
        this.isExpand = false;
    }
    public Order(String id, int totalAmount, Date createDateTime, List<DetailOrder> ordersDetail, String status) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.createDateTime = createDateTime;
        this.ordersDetail = ordersDetail;
        this.isExpand = false;
        switch (status) {
            case "Order placed":
                this.state = new OrderPlacedState();
                break;
            case "Packaged":
                this.state = new PackagedState();
                break;
            case "Delivered":
                this.state = new ShippedState();
                break;
            case "Undelivered":
                this.state = new UndeliveredState();
                break;
            case "Completed":
                this.state = new CompletedState();
                break;
            case "Cancelled":
                this.state = new CancelledState();
                break;
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
        this.state.setOrder(this);
    }
    public OrderState getState(){
        return state;
    }
    public void changeState(OrderState newState) {
        this.state = newState;
        this.state.setOrder(this);
    }
    public String getStatus() {
        return this.state.getStatus();
    }
    public String acceptState() {
        return this.state.onAccept();
    }
    public String cancelState() {
        return this.state.onCancel();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public String getCreateDateTimeString() {
        return dateFormat.format(createDateTime);
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public List<DetailOrder> getOrdersDetail() {
        return ordersDetail;
    }

    public void setOrdersDetail(List<DetailOrder> ordersDetail) {
        this.ordersDetail = ordersDetail;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public void toggleExpand() {
        isExpand = !isExpand;
    }


    public void setStatus(String status) {

    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
