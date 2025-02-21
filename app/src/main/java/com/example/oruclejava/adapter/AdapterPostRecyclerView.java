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
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.liked = !holder.liked;

                if (holder.liked){
                    holder.postModel.setLikeCount(holder.postModel.getLikeCount()+1);
                    holder.likeButton.setImageResource(R.drawable.ic_heart_fill);
                    holder.likeNumber.setText(String.valueOf(holder.postModel.getLikeCount()));
                } else {
                    holder.postModel.setLikeCount(holder.postModel.getLikeCount()-1);
                    holder.likeButton.setImageResource(R.drawable.ic_heart_stroke);
                    holder.likeNumber.setText(String.valueOf(holder.postModel.getLikeCount()));
                }

            }
        });
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
        private ImageView likeButton;
        private TextView commentNumber;
        private ImageView commentButton;
        private boolean liked = false;
        private PostModel postModel;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.post_avatar);
            username = itemView.findViewById(R.id.post_username);
            content = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            likeNumber = itemView.findViewById(R.id.like_number);
            commentNumber = itemView.findViewById(R.id.comment_number);
            likeButton = itemView.findViewById(R.id.like_img_btn);
            commentButton = itemView.findViewById(R.id.comment_img_btn);
            likeButton.setImageResource(
                    liked ? R.drawable.ic_heart_fill : R.drawable.ic_heart_stroke);
        }

        void bind(PostModel post){
            postModel = post;
            userAvatar.setImageResource(post.getAvatar());
            username.setText(post.getUsername());
            content.setText(post.getContent());
            postImage.setImageResource(post.getImageId());
            likeNumber.setText(String.valueOf(post.getLikeCount()));
            commentNumber.setText(String.valueOf(post.getCommentCount()));

        }
    }
}
