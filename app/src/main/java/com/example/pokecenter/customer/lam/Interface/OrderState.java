package com.example.pokecenter.customer.lam.Interface;

import com.example.pokecenter.customer.lam.StateClass.Order;

public interface OrderState {
    void setOrder(Order order);
    void updateState(Order order);
    String getStatus();
}
