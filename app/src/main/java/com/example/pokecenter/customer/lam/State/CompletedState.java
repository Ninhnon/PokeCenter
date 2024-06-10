package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;

public class CompletedState implements OrderState {
    Order order;
    @Override
    public void setOrder(Order order) {
        this.order =order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Complete.");
    }
    @Override
    public String getStatus() {
        return "Delivery completed";
    }
}

