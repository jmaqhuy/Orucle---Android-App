package com.example.oruclejava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.adapter.AdapterChatInRoom;
import com.example.oruclejava.models.ChatMessage;
import com.example.oruclejava.utils.Constants;
import com.example.oruclejava.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String senderId;
    private String receiverId;
    private String encodedImage;
    private String name;
    private ArrayList<ChatMessage> messages;
    private ImageView sendButton;

    private RoundedImageView userAvatar;
    private TextView username;
    private EditText editTextMessage;

    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private AdapterChatInRoom adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();
        listenMessage();

        editTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                recyclerView.postDelayed(() -> recyclerView.smoothScrollToPosition(messages.size() - 1), 500);
            }
        });

        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    sendButton.setVisibility(View.GONE);
                } else {
                    sendButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(v -> {
            sendMessage();
        });
    }

    private void listenMessage() {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, senderId)
                .addSnapshotListener(eventListener);

    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = messages.size();
            for (DocumentChange document : value.getDocumentChanges()) {
                if (document.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSenderId(document.getDocument().getString(Constants.KEY_SENDER_ID));
                    chatMessage.setReceiverId(document.getDocument().getString(Constants.KEY_RECEIVER_ID));
                    chatMessage.setText(document.getDocument().getString(Constants.KEY_MESSAGE));
                    chatMessage.setTimestamp(
                            Objects.requireNonNull(
                                    document.getDocument().getDate(Constants.KEY_TIMESTAMP)).toString());

                    messages.add(chatMessage);
                }
            }
            Collections.sort(messages, (obj1, obj2) -> obj1.getTimestamp().compareTo(obj2.getTimestamp()));
            if (count == 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(messages.size(), messages.size());
                recyclerView.smoothScrollToPosition(messages.size() - 1);
            }
        }
    };

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, senderId);
        message.put(Constants.KEY_RECEIVER_ID, receiverId);
        message.put(Constants.KEY_MESSAGE, editTextMessage.getText().toString().trim());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        editTextMessage.setText(null);
    }

    void init() {
        senderId = FirebaseAuth.getInstance().getUid();
        receiverId = getIntent().getStringExtra(Constants.KEY_USER_ID);
        encodedImage = getIntent().getStringExtra(Constants.KEY_IMAGE);
        name = getIntent().getStringExtra(Constants.KEY_NAME);
        sendButton = findViewById(R.id.send_button);

        findViewById(R.id.back).setOnClickListener(v -> {
            finish();
        });

        userAvatar = (findViewById(R.id.toolbar)).findViewById(R.id.user_avatar);
        username = findViewById(R.id.username);
        editTextMessage = findViewById(R.id.editTextMessage);

        userAvatar.setImageBitmap(getUserImage(encodedImage));
        username.setText(name);
        messages = new ArrayList<>();

        adapter = new AdapterChatInRoom(messages, senderId, encodedImage);
        recyclerView = findViewById(R.id.messageView);
        recyclerView.setAdapter(adapter);
        database = FirebaseFirestore.getInstance();
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}