package com.example.pokecenter.customer.lam.State.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private String id;
    public Notification(String id, String title, String content, Date sentDate, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.sentDate = sentDate;
        this.type = type;
        this.state = new UnreadState(this);
    }
    public void setState(NotificationState state) {
        this.state = state;
    }
    public void markAsRead() {
        state.markAsRead();
    }
    public void dismiss() {
        state.dismiss();
    }
    public String getDisplayTitle() {
        return state.getDisplayTitle();
    }
    public String getDisplayContent() {
        return state.getDisplayContent();
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }private String title;
    private String content;
    private Date sentDate;
    private String type;
    private NotificationState state;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

}