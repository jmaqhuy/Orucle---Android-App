package com.example.oruclejava.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.MessageActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.models.ChatMessage;
import com.example.oruclejava.models.MessageModel;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.MessageViewHolder> {

    private ArrayList<ChatMessage> messages;
    private final String myId = FirebaseAuth.getInstance().getUid();

    public AdapterMessage(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public AdapterMessage.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message, parent, false);
        return new AdapterMessage.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessage.MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
        holder.itemView.setOnClickListener(view ->{
            Intent intent = new Intent(view.getContext(), MessageActivity.class);
            if (myId.equals(messages.get(position).getSenderId())){
                intent.putExtra(Constants.KEY_USER_ID, messages.get(position).getReceiverId());
            } else {
                intent.putExtra(Constants.KEY_USER_ID, messages.get(position).getSenderId());
            }

            intent.putExtra(Constants.KEY_IMAGE, messages.get(position).getConversionImage());
            intent.putExtra(Constants.KEY_NAME, messages.get(position).getConversionName());
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView username;
        private TextView lastMessage;
        private ImageView unread;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = (itemView.findViewById(R.id.view2)).findViewById(R.id.user_avatar);
            username = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.last_message);
            unread = itemView.findViewById(R.id.unread);
        }

        void bind(ChatMessage chatMessage){
            avatar.setImageBitmap(decodeImage(chatMessage.getConversionImage()));
            username.setText(String.valueOf(chatMessage.getConversionName()));
            lastMessage.setText(String.valueOf(chatMessage.getText()));
//            unread.setVisibility(messageModel.isUnread()? View.VISIBLE : View.INVISIBLE);
        }
    }
    private static Bitmap decodeImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
