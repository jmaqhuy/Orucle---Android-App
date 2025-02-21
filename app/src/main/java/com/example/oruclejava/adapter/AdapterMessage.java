package com.example.oruclejava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.R;
import com.example.oruclejava.models.MessageModel;

import java.util.ArrayList;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.MessageViewHolder> {

    private ArrayList<MessageModel> messages;

    public AdapterMessage(ArrayList<MessageModel> messages) {
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

        void bind(MessageModel messageModel){
            avatar.setImageResource(messageModel.getAvatarId());
            username.setText(String.valueOf(messageModel.getUsername()));
            lastMessage.setText(String.valueOf(messageModel.getLastMessage()));
            unread.setVisibility(messageModel.isUnread()? View.VISIBLE : View.INVISIBLE);
        }
    }
}
