package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;

public class OrderPlacedState implements OrderState {
    Order order;
    @Override
    public void setOrder(Order order) {
        this.order =order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Order Placed state.");
        order.changeState(new PackagedState());
    }
    @Override
    public String getStatus() {
        return "Order placed";
    }
}
