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
import com.example.oruclejava.utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String senderId, receiverId, receiverImage, receiverName, senderName;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView btnSendMessage;
    private RoundedImageView userAvatar;
    private TextView username;
    private EditText editTextMessage;
    private FirebaseFirestore database;
    private AdapterChatInRoom adapter;
    private String conversionID = null;

    private String senderImage;

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
        receiverImage = getIntent().getStringExtra(Constants.KEY_IMAGE);
        receiverName = getIntent().getStringExtra(Constants.KEY_NAME);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        senderName = preferenceManager.getString(Constants.KEY_SENDER_NAME);
        senderImage = preferenceManager.getString(Constants.KEY_SENDER_IMAGE);

        userAvatar.setImageBitmap(decodeImage(receiverImage));
        username.setText(receiverName);

        chatMessages = new ArrayList<>();
        adapter = new AdapterChatInRoom(chatMessages, senderId, receiverImage);
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
        chatMessages.sort(Comparator.comparing(ChatMessage::getDate));

        for (ChatMessage msg : chatMessages) {
            Log.d("DEBUG_TIMESTAMP", "Message: " + msg.getText() + " - " + msg.getDate());
        }

        if (initialSize == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeInserted(initialSize, chatMessages.size() - initialSize);
        }

        scrollToLastMessage();
        if (conversionID == null) {
            checkForConversion();
        }
    };

    private void addConversion(HashMap<String, Object> conversion) {
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .add(conversion)
                .addOnSuccessListener(documentReference -> {
                   conversionID = documentReference.getId();
                });
    }

    private void updateConversion(String message){
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .document(conversionID)
                .update(Constants.KEY_LAST_MESSAGE, message,
                        Constants.KEY_TIMESTAMP, new Date());

    }

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
        if (conversionID == null){
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, senderId);
            conversion.put(Constants.KEY_RECEIVER_ID, receiverId);
            conversion.put(Constants.KEY_SENDER_NAME, senderName);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverName);
            conversion.put(Constants.KEY_SENDER_IMAGE, senderImage);
            conversion.put(Constants.KEY_RECEIVER_IMAGE, receiverImage);
            conversion.put(Constants.KEY_LAST_MESSAGE, content);
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        } else {
            updateConversion(content);
        }
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

    private void checkForConversion(){
        if (!chatMessages.isEmpty()){
            checkConversationRemotely(senderId, receiverId);
            checkConversationRemotely(receiverId, senderId);
        }
    }

    private void checkConversationRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversationListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()){
            DocumentSnapshot document = task.getResult().getDocuments().get(0);
            conversionID = document.getId();
        }
    };
}
