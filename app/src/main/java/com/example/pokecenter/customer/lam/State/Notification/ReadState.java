package com.example.pokecenter.customer.lam.State.Notification;

import com.example.pokecenter.customer.lam.API.FirebaseSupportCustomer;

import java.io.IOException;

public class ReadState implements NotificationState {
    private Notification notification;
    public ReadState(Notification notification) {
        this.notification = notification;
    }
    @Override
    public void markAsRead() {
        // Already read, so do nothing
        System.out.println("Notification is already read");
    }
    @Override
    public void dismiss() {
        try {
            new FirebaseSupportCustomer().changeStatusNotification(notification.getId(),"dismiss");
        } catch (IOException e) {
        }
        notification.setState(new DismissedState(notification));
    }
    @Override
    public String getDisplayTitle() {
        return notification.getTitle();
    }
    @Override
    public String getDisplayContent() {
        return notification.getContent();
    }
}
