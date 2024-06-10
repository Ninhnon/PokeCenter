package com.example.pokecenter.customer.lam.StateClass;

import com.example.pokecenter.customer.lam.Interface.OrderState;
public class PackagedState implements OrderState {
    Order order;
    @Override
    public void setOrder(Order order) {
        this.order =order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Packaged state.");
        order.changeState(new DeliveredState());
    }
    @Override
    public String getStatus() {
        return "Packaged";
    }
}
