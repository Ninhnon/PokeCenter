package com.example.pokecenter.vender.Model.ChatRoom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ChatRoomInterface {
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState);

    void onChatRoomItemClick(ChatRoom c);
}
