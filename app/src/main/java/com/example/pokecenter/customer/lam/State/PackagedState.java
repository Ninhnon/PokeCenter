package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;
import com.example.pokecenter.customer.lam.Model.order.Order;
public class PackagedState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Packaged state.");
        order.changeState(new ShippedState());
    }
    @Override
    public String getStatus() {
        return "Packaged";
    }
    @Override
    public String onAccept() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Shipped");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to accept order.";
        }
        order.changeState(new ShippedState());
        return "Order shipped.";
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

