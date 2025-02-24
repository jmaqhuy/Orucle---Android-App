package com.example.oruclejava.fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oruclejava.MainActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.adapter.AdapterPostRecyclerView;
import com.example.oruclejava.models.PostModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Random;

public class ProfileFragment extends Fragment {

    private ArrayList<PostModel> ownPost;
    private RoundedImageView user_avatar;
    private NestedScrollView nestedScrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        RecyclerView own_post_list = view.findViewById(R.id.own_post_list);
        fakeData(((MainActivity) getActivity()).getUsername());
        AdapterPostRecyclerView adapter = new AdapterPostRecyclerView(ownPost);
        own_post_list.setLayoutManager(new LinearLayoutManager(getContext()));
        own_post_list.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_follow).setVisibility(View.GONE);
        view.findViewById(R.id.btn_message).setVisibility(View.GONE);
        nestedScrollView = view.findViewById(R.id.main);
        if (user_avatar == null){
            user_avatar = view.findViewById(R.id.user_avatar);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).getAvatar(user_avatar);
            }
        }
        TextView displayName = view.findViewById(R.id.displayname);
        displayName.setText(((MainActivity) getActivity()).getUsername());
        Log.d("KEY_NAME", "Assign username");
        //TODO: create listener even for btn_edit_profile

    }
    public void scrollToTop() {
        if (nestedScrollView != null) {
            nestedScrollView.smoothScrollTo(0, 0, 1000);
        }
    }

    private void fakeData(String username) {
        Random random = new Random();
        ownPost = new ArrayList<>();
        String[] contents = {
                "What a beautiful day!", "Just finished my new project!", "Enjoying a cup of coffee ☕",
                "Love this place ❤️", "Excited for the weekend!", "Coding all night 😴",
                "Having fun with friends!", "New haircut, new me ✂️", "Nature is so beautiful 🌳",
                "Movie night 🍿", "Time to hit the gym! 💪", "Exploring new places 🌍",
                "Trying out a new recipe 🍲", "Weekend vibes! 🎉", "Happy to be here! 😊",
                "This view is amazing! 🏔️", "Feeling grateful today 💖", "Dream big, work hard 🚀",
                "Another productive day! 🏆", "Let’s go on an adventure! 🌏", "Life is a journey! 🚗",
                "Sunset lover 🌅", "Beach time! 🏖️", "Stay positive! ✨", "Chasing dreams ✈️",
                "Good vibes only! 😎", "Happiness is a choice! 🎶", "Food is life 🍕",
                "Enjoying the little things in life 🌸", "Weekend road trip! 🚙"
        };

        int[] avatars = {R.drawable.boy_avatar, R.drawable.girl_avatar};
        int[] images = {R.drawable.a, R.drawable.b, R.drawable.c};

        for (int i = 0; i < 20; i++) { // Tạo 50 post ngẫu nhiên
            PostModel post = new PostModel();
            post.setAvatar(avatars[random.nextInt(avatars.length)]);
            post.setUsername(username);
            post.setContent(contents[random.nextInt(contents.length)]);
            post.setImageId(images[random.nextInt(images.length)]);
            post.setLikeCount(random.nextInt(500)); // Số like ngẫu nhiên từ 0-499
            post.setCommentCount(random.nextInt(200)); // Số comment ngẫu nhiên từ 0-199

            ownPost.add(post);
        }
    }
}