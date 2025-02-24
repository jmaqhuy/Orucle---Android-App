package com.example.oruclejava.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oruclejava.MainActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.adapter.AdapterMessage;
import com.example.oruclejava.adapter.AdapterOnlineUserMessage;
import com.example.oruclejava.models.ChatMessage;
import com.example.oruclejava.models.OnlineUserModel;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MessageFragment extends Fragment {
    private RecyclerView onlineUserRecycler;
    private RecyclerView messagesRecycler;
    private ArrayList<OnlineUserModel> onlineUserModels;
    private ArrayList<ChatMessage> chatMessages;
    private FirebaseFirestore database;
    private String conservationID = null;
    private String myId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        init(view);

        fakeData();
        AdapterOnlineUserMessage onlineUsersAdapter = new AdapterOnlineUserMessage(onlineUserModels);
        onlineUserRecycler.setAdapter(onlineUsersAdapter);
        onlineUserRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        AdapterMessage adapterMessage = new AdapterMessage(chatMessages);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecycler.setAdapter(adapterMessage);
        listenerConversations();
        return view;
    }

    private void init(View view){
        onlineUserModels = new ArrayList<>();
        chatMessages = new ArrayList<>();
        onlineUserRecycler = view.findViewById(R.id.online_users);
        messagesRecycler = view.findViewById(R.id.messages);
        database = FirebaseFirestore.getInstance();
        myId = FirebaseAuth.getInstance().getUid();
    }

    private void listenerConversations(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_SENDER_ID, myId)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, myId)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {

                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(senderId);
                    chatMessage.setReceiverId(receiverId);

                    if (Objects.equals(myId, senderId)){
                        chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME));
                        chatMessage.setConversionImage(documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE));

                    } else {
                        chatMessage.setConversionName(documentChange.getDocument().getString(Constants.KEY_SENDER_NAME));
                        chatMessage.setConversionImage(documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE));
                    }
                    chatMessage.setText(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                    chatMessage.setConversionId(documentChange.getDocument().getString(Constants.KEY_CONVERSATION_ID));
                    chatMessage.setDate(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessages.add(chatMessage);

                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for (int i = 0 ; i < chatMessages.size(); i++){
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (chatMessages.get(i).getSenderId().equals(senderId) && chatMessages.get(i).getReceiverId().equals(receiverId)){
                            chatMessages.get(i).setText(documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE));
                            chatMessages.get(i).setConversionId(documentChange.getDocument().getString(Constants.KEY_CONVERSATION_ID));
                            break;
                        }
                    }
                }

            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj2.getDate().compareTo(obj1.getDate()));
            messagesRecycler.getAdapter().notifyDataSetChanged();
            messagesRecycler.smoothScrollToPosition(0);
        }
    };


    private void fakeData(){
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.girl_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.girl_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.girl_avatar, true));
        onlineUserModels.add(new OnlineUserModel(R.drawable.boy_avatar, true));
    }
}