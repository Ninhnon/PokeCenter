package com.example.pokecenter.customer.lam.State.Notification;

public class DismissedState implements NotificationState {
    private Notification notification;
    public DismissedState(Notification notification) {
        this.notification = notification;
    }
    @Override
    public void markAsRead() {
        System.out.println("Cannot mark dismissed notification as read");
    }
    @Override
    public void dismiss() {
        System.out.println("Notification is already dismissed");
    }
    @Override
    public String getDisplayTitle() {
        return "[Dismissed] " + notification.getTitle();
    }
    @Override
    public String getDisplayContent() {
        return "This notification has been dismissed.";
    }
}