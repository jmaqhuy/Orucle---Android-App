package com.example.oruclejava.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oruclejava.R;
import com.example.oruclejava.adapter.AdapterMessage;
import com.example.oruclejava.adapter.AdapterOnlineUserMessage;
import com.example.oruclejava.models.MessageModel;
import com.example.oruclejava.models.OnlineUserModel;

import java.util.ArrayList;
import java.util.Random;

public class MessageFragment extends Fragment {
    Random random = new Random();
    private RecyclerView onlineUserRecycler;
    private RecyclerView messagesRecycler;
    private ArrayList<OnlineUserModel> onlineUserModels = new ArrayList<>();
    private ArrayList<MessageModel> messageModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        onlineUserRecycler = view.findViewById(R.id.online_users);
        messagesRecycler = view.findViewById(R.id.messages);

        fakeData();
        AdapterOnlineUserMessage onlineUsersAdapter = new AdapterOnlineUserMessage(onlineUserModels);
        onlineUserRecycler.setAdapter(onlineUsersAdapter);
        onlineUserRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        AdapterMessage adapterMessage = new AdapterMessage(messageModels);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesRecycler.setAdapter(adapterMessage);


        return view;
    }

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

        String[] usernames = {"Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Helen", "Ivy", "Jack"};
        String[] messages = {"Hello!", "How are you?", "See you later!", "Good morning!", "Let's meet up.", "Check this out!", "Happy birthday!", "Miss you!", "Call me!", "On my way."};

        for (int i = 0; i < 10; i++) {
            int avatarId = random.nextBoolean() ? R.drawable.boy_avatar : R.drawable.girl_avatar;
            String username = usernames[random.nextInt(usernames.length)];
            String lastMessage = messages[random.nextInt(messages.length)];
            boolean unread = random.nextBoolean();
            messageModels.add(new MessageModel(avatarId, username, lastMessage, unread));
        }
    }
}