package com.example.oruclejava.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.R;
import com.example.oruclejava.models.ChatMessage;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;



public class AdapterChatInRoom extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatMessage> messages;
    private String senderId;
    private String receiverAvatarString;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public AdapterChatInRoom(ArrayList<ChatMessage> messages, String senderId, String receiverAvatar) {
        this.messages = messages;
        this.senderId = senderId;
        this.receiverAvatarString = receiverAvatar;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_send_message, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
            return new ReceivMessageHolder(view, receiverAvatarString);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageHolder) holder).bind(messages.get(position));
        } else {
            ((ReceivMessageHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageHolder extends RecyclerView.ViewHolder{
        private TextView textMessage;
        private TextView textTime;
        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }

        void bind(ChatMessage message){
            textMessage.setText(message.getText());
            textTime.setText(getTextDate(message.getDate()));
        }
    }

    static class ReceivMessageHolder extends RecyclerView.ViewHolder{
        private TextView textMessage;
        private TextView textTime;
        private RoundedImageView receiverAvatar;
        private String receiverAvatarString;
        public ReceivMessageHolder(@NonNull View itemView, String receiverAvatarString) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
            receiverAvatar = itemView.findViewById(R.id.receiverAvatar);
            this.receiverAvatarString = receiverAvatarString;
        }

        void bind(ChatMessage message){
            textMessage.setText(message.getText());
            textTime.setText(getTextDate(message.getDate()));
            receiverAvatar.setImageBitmap(getUserImage(receiverAvatarString));
        }
        private Bitmap getUserImage(String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }


    }
    private static String getTextDate(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        Calendar messageCalendar = Calendar.getInstance();
        messageCalendar.setTime(date);

        Calendar todayCalendar = Calendar.getInstance();

        if (messageCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                messageCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
            return timeFormat.format(date); // Nếu là hôm nay -> Chỉ hiện giờ
        } else {
            return dateFormat.format(date); // Nếu là ngày khác -> Hiện cả ngày tháng năm
        }
    }
}
