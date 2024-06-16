package com.example.pokecenter.customer.lam.State.Notification;

public interface NotificationState {
    void markAsRead();
    void dismiss();
    String getDisplayTitle();
    String getDisplayContent();
}
