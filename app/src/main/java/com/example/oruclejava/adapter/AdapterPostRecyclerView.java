package com.example.oruclejava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.models.PostModel;
import com.example.oruclejava.R;

import java.util.ArrayList;

public class AdapterPostRecyclerView extends
        RecyclerView.Adapter<AdapterPostRecyclerView.PostViewHolder> {

    private ArrayList<PostModel> posts;

    public AdapterPostRecyclerView(ArrayList<PostModel> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public AdapterPostRecyclerView.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_feed, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPostRecyclerView.PostViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView userAvatar;
        private TextView username;
        private TextView content;
        private ImageView postImage;
        private TextView likeNumber;
        private TextView commentNumber;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.post_avatar);
            username = itemView.findViewById(R.id.post_username);
            content = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            likeNumber = itemView.findViewById(R.id.like_number);
            commentNumber = itemView.findViewById(R.id.comment_number);

        }

        void bind(PostModel post){
            userAvatar.setImageResource(post.getAvatar());
            username.setText(post.getUsername());
            content.setText(post.getContent());
            postImage.setImageResource(post.getImageId());
            likeNumber.setText(String.valueOf(post.getLikeCount()));
            commentNumber.setText(String.valueOf(post.getCommentCount()));
        }
    }
}
