package com.example.pokecenter.customer.lam.State;

import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;
import com.example.pokecenter.customer.lam.Model.order.Order;
public class ShippedState implements OrderState {
    private Order order;
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }
    @Override
    public void updateState(Order order) {
        System.out.println("Processing order in Shipped state.");
        order.changeState(new CompletedState());
    }
    @Override
    public String getStatus() {
        return "Delivered";
    }
    @Override
    public String onAccept() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Completed");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to accept order.";
        }
        order.changeState(new CompletedState());
        return "Order completed.";
    }
    @Override
    public String onCancel() {
        try {
            new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), "Undelivered");
            new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
        } catch (IOException e) {
            return "Failed to cancel order.";
        }
        order.changeState(new UndeliveredState());
        return "Order not delivered.";
    }
}


