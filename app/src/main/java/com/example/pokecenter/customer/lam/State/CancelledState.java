package com.example.pokecenter.customer.lam.State;
import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.customer.lam.Model.order.Order;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;

public class CancelledState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Cancelled state.");
    }
    @Override
    public String getStatus() {
        return "Cancelled";
    }
    @Override
    public String onAccept() {
        return "Order cancelled. Cannot accept.";
    }
    @Override
    public String onCancel() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Cancelled");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to cancel order.";
        }
        return "Order already cancelled.";
    }
}

