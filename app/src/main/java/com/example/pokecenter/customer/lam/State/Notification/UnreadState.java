package com.example.pokecenter.customer.lam.State.Notification;

import com.example.pokecenter.customer.lam.API.FirebaseSupportCustomer;

import java.io.IOException;

public class UnreadState implements NotificationState {
    private Notification notification;

    public UnreadState(Notification notification) {
        this.notification = notification;
    }
    @Override
    public void markAsRead() {
        notification.setState(new ReadState(notification));
        try {
            new FirebaseSupportCustomer().changeStatusNotification(notification.getId(),"read");
        } catch (IOException e) {
        }
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
        return "[New] " + notification.getTitle();
    }
    @Override
    public String getDisplayContent() {
        return notification.getContent();
    }
}
