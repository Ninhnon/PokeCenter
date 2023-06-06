package com.example.pokecenter.admin.Quan.AdminTab.FirebaseAPI;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokecenter.admin.Quan.AdminTab.Model.MessageSender.AdminChatUser;
import com.example.pokecenter.admin.Quan.AdminTab.Model.MessageSender.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseFetchChat {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String currentEmail;

    public FirebaseFetchChat(Context context) {
        this.context = context;
    }

    public void getChatUserListFromFirebase(FirebaseCallback<ArrayList<AdminChatUser>> firebaseCallback) {
        ArrayList<AdminChatUser> chatUsers = new ArrayList<>();

        //Get current user's email
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentEmail = firebaseUser.getEmail();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("chats");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey().replace(",",".").equals(currentEmail)) {
                        try {
                            for (DataSnapshot chatUserSnapshot : dataSnapshot.getChildren()) {
                                //Get user info
                                String email = chatUserSnapshot.getKey().replace(",", ".");
                                String avatar = chatUserSnapshot.child("avatar").getValue(String.class);
                                String name = chatUserSnapshot.child("name").getValue(String.class);
                                int role = chatUserSnapshot.child("role").getValue(int.class);
                                //Get user's message history

                                ArrayList<Message> messages = new ArrayList<Message>();
                                for (DataSnapshot messageSnapshot : chatUserSnapshot.child("messages").getChildren()) {
                                    String id = messageSnapshot.getKey();
                                    String content = messageSnapshot.child("content").getValue(String.class);
                                    String sentTime = messageSnapshot.child("sentTime").getValue(String.class);
                                    boolean hasSeen = messageSnapshot.child("hasSeen").getValue(boolean.class);
                                    Message message = new Message(id, content, sentTime, hasSeen);
                                    //Add to messages
                                    messages.add(message);
                                }

                                AdminChatUser user = new AdminChatUser(email, avatar, name, role, messages);
                                //Add to chatUsers
                                chatUsers.add(user);
                            }
                        } catch (Exception e) {
                            Log.e("getChatUserListFromFirebase", e.toString());
                        }
                        break;
                    }
                }
                firebaseCallback.onCallback(chatUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void updateHasSeenStateForMessage(String userEmail) {
        // Get current user's email
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentEmail = firebaseUser.getEmail();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("chats/"
                + currentEmail.replace(".", ",") + "/"
                + userEmail.replace(".", ",")
                + "/messages"
        );

        // Create a map to hold the updates
        HashMap<String, Object> updateMap = new HashMap<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String messageId = dataSnapshot.getKey();
                    DatabaseReference hasSeenRef = dataSnapshot.getRef().child("hasSeen");

                    // Add the update to the map
                    updateMap.put(messageId + "/hasSeen", true);
                }

                // Perform the batch update
                myRef.updateChildren(updateMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Batch update successful
                                // Handle any additional operations or UI updates
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to perform batch update
                                // Handle the error
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if the read operation is canceled
            }
        });
    }

}
