package com.example.oruclejava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.R;
import com.example.oruclejava.models.OnlineUserModel;

import java.util.ArrayList;

public class AdapterOnlineUserMessage extends RecyclerView.Adapter<AdapterOnlineUserMessage.OnlineUserViewHolder> {

    private ArrayList<OnlineUserModel> userModels = new ArrayList<>();

    public AdapterOnlineUserMessage(ArrayList<OnlineUserModel> onlineUserModels){
        this.userModels = onlineUserModels;
    }

    @NonNull
    @Override
    public OnlineUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnlineUserViewHolder(
                LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.user_avatar_online_status,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineUserViewHolder holder, int position) {
        holder.bind(userModels.get(position));
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    static class OnlineUserViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private RelativeLayout onlineStatus;

        public OnlineUserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.user_avatar);
            onlineStatus = itemView.findViewById(R.id.online_status);
        }

        void bind(OnlineUserModel userModel){
            avatar.setImageResource(userModel.getAvatarId());
            int visibility = userModel.isOnlineStatus() ? View.VISIBLE : View.INVISIBLE;
            onlineStatus.setVisibility(visibility);
        }
    }
}
