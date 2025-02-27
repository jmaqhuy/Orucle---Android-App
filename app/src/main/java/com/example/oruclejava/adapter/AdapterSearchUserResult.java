package com.example.oruclejava.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.activity.MessageActivity;
import com.example.oruclejava.activity.ProfileActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.models.UserModel;
import com.example.oruclejava.utils.Constants;

import java.util.ArrayList;

public class AdapterSearchUserResult extends RecyclerView.Adapter<AdapterSearchUserResult.ResultViewHolder> {

    private ArrayList<UserModel> users;

    public AdapterSearchUserResult(ArrayList<UserModel> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public AdapterSearchUserResult.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_result, parent, false);
        return new AdapterSearchUserResult.ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchUserResult.ResultViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView username;
        private Button follow;
        private Button message;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.user_avatar);
            username = itemView.findViewById(R.id.username);
            follow = itemView.findViewById(R.id.follow);
            message = itemView.findViewById(R.id.inbox);
        }

        void bind(UserModel userModel){
            avatar.setImageBitmap(getUserImage(userModel.getEncodedAvatar()));
            username.setText(userModel.getUsername());
            follow.setText(userModel.isFollowing() ? "Following" : "Follow");
            message.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra(Constants.KEY_USER_ID, userModel.getUserId());
                intent.putExtra(Constants.KEY_IMAGE, userModel.getEncodedAvatar());
                intent.putExtra(Constants.KEY_NAME, userModel.getUsername());
                view.getContext().startActivity(intent);
            });

            avatar.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                intent.putExtra(Constants.KEY_USER_ID, userModel.getUserId());
                intent.putExtra(Constants.KEY_IMAGE, userModel.getEncodedAvatar());
                intent.putExtra(Constants.KEY_NAME, userModel.getUsername());
                view.getContext().startActivity(intent);
            });
        }

        private Bitmap getUserImage(String encodedImage){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

    }
}
