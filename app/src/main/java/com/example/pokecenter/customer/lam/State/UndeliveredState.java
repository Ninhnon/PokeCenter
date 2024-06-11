package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;

public class UndeliveredState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Undelivered state.");
    }
    @Override
    public String getStatus() {
        return "Undelivered";
    }
    @Override
    public String onAccept() {
        return "Order not delivered. Cannot accept.";
    }
    @Override
    public String onCancel() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Undelivered");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to cancel order.";
        }
        return "Order already undelivered.";
    }
}

