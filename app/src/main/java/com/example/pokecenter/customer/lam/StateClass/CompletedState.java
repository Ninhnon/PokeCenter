package com.example.pokecenter.customer.lam.StateClass;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.customer.lam.Model.order.Order;

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

