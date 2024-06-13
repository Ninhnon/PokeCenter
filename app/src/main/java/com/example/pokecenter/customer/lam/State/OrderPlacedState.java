package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;
import com.example.pokecenter.customer.lam.Model.order.Order;
public class OrderPlacedState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
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
    @Override
    public String onAccept() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Packaged");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to accept order.";
        }
        order.changeState(new PackagedState());
        return "Order accepted and packaged.";
    }

    @Override
    public String onCancel() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Cancelled");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to cancel order.";
        }
        order.changeState(new CancelledState());
        return "Order cancelled.";
    }
}

