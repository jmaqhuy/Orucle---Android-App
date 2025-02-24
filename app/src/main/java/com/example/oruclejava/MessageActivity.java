package com.example.oruclejava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.adapter.AdapterChatInRoom;
import com.example.oruclejava.models.ChatMessage;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String senderId, receiverId, encodedImage, name;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView btnSendMessage;
    private RoundedImageView userAvatar;
    private TextView username;
    private EditText editTextMessage;
    private FirebaseFirestore database;
    private AdapterChatInRoom adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initViews();
        initListeners();
        initData();
        listenForMessages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.messageView);
        btnSendMessage = findViewById(R.id.send_button);
        userAvatar = findViewById(R.id.toolbar).findViewById(R.id.user_avatar);
        username = findViewById(R.id.username);
        editTextMessage = findViewById(R.id.editTextMessage);
    }

    private void initListeners() {
        btnSendMessage.setOnClickListener(v -> sendMessage());

        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toggleSendButton(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void initData() {
        senderId = FirebaseAuth.getInstance().getUid();
        receiverId = getIntent().getStringExtra(Constants.KEY_USER_ID);
        encodedImage = getIntent().getStringExtra(Constants.KEY_IMAGE);
        name = getIntent().getStringExtra(Constants.KEY_NAME);

        userAvatar.setImageBitmap(decodeImage(encodedImage));
        username.setText(name);

        chatMessages = new ArrayList<>();
        adapter = new AdapterChatInRoom(chatMessages, senderId, encodedImage);
        recyclerView.setAdapter(adapter);

        database = FirebaseFirestore.getInstance();
    }

    private void listenForMessages() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereIn(Constants.KEY_SENDER_ID, Arrays.asList(senderId, receiverId))
                .whereIn(Constants.KEY_RECEIVER_ID, Arrays.asList(senderId, receiverId))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null || value == null) return;
        int initialSize = chatMessages.size();

        for (DocumentChange document : value.getDocumentChanges()) {
            if (document.getType() == DocumentChange.Type.ADDED) {
                processNewMessage(document);
            }
        }
        Collections.sort(chatMessages, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));

        for (ChatMessage msg : chatMessages) {
            Log.d("DEBUG_TIMESTAMP", "Message: " + msg.getText() + " - " + msg.getDate());
        }

        if (initialSize == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeInserted(initialSize, chatMessages.size() - initialSize);
        }

        scrollToLastMessage();
    };

    private void processNewMessage(DocumentChange document) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(document.getDocument().getString(Constants.KEY_SENDER_ID));
        chatMessage.setReceiverId(document.getDocument().getString(Constants.KEY_RECEIVER_ID));
        chatMessage.setText(Objects.requireNonNull(document.getDocument().getString(Constants.KEY_MESSAGE)).trim());
        chatMessage.setDate(document.getDocument().getDate(Constants.KEY_TIMESTAMP));

        chatMessages.add(chatMessage);
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (content.isEmpty()) {
            editTextMessage.setText(null);
            return;
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, senderId);
        message.put(Constants.KEY_RECEIVER_ID, receiverId);
        message.put(Constants.KEY_MESSAGE, content);
        message.put(Constants.KEY_TIMESTAMP, new Date());

        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        editTextMessage.setText(null);
    }

    private void scrollToLastMessage() {
        if (!chatMessages.isEmpty()) {
            recyclerView.postDelayed(() ->
                    recyclerView.smoothScrollToPosition(chatMessages.size() - 1), 300);
        }
    }

    private void toggleSendButton(boolean isVisible) {
        btnSendMessage.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
