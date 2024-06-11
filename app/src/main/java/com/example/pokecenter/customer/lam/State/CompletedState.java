package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;

public class CompletedState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Completed state.");
    }
    @Override
    public String getStatus() {
        return "Delivery completed";
    }
    @Override
    public String onAccept() {
        return "Order already completed.";
    }
    @Override
    public String onCancel() {
        return "Order already completed.";
    }
}

