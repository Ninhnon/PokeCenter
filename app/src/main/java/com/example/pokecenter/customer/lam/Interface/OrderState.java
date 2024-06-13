package com.example.pokecenter.customer.lam.Interface;

import com.example.pokecenter.customer.lam.Model.order.Order;

public interface OrderState {
    void setOrder(Order order);
    void updateState(Order order);
    String getStatus();
    String onAccept();
    String onCancel();
}


