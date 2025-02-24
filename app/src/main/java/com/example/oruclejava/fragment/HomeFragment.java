package com.example.oruclejava.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oruclejava.MainActivity;
import com.example.oruclejava.WelcomeActivity;
import com.example.oruclejava.models.PostModel;
import com.example.oruclejava.R;
import com.example.oruclejava.adapter.AdapterPostRecyclerView;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class HomeFragment extends Fragment {
    private final ArrayList<PostModel> posts = new ArrayList<>();

    private RoundedImageView userAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (userAvatar == null){
            userAvatar = view.findViewById(R.id.user_avatar);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).getAvatar(userAvatar);
            }
        }
        userAvatar.setOnClickListener(view1 -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToProfileFragment();
            }
        });
        RecyclerView homeRecyclerView = view.findViewById(R.id.home_recycler_view);
        fakeData();
        AdapterPostRecyclerView adapter = new AdapterPostRecyclerView(posts);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setAdapter(adapter);

        return view;
    }


    private void fakeData() {
        Random random = new Random();
        String[] usernames = {
                "Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Hannah",
                "Ian", "Jack", "Kelly", "Liam", "Mia", "Noah", "Olivia", "Paul",
                "Quinn", "Ryan", "Sophia", "Tom", "Uma", "Victor", "Wendy", "Xavier",
                "Yara", "Zane"
        };

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
            post.setUsername(usernames[random.nextInt(usernames.length)]);
            post.setContent(contents[random.nextInt(contents.length)]);
            post.setImageId(images[random.nextInt(images.length)]);
            post.setLikeCount(random.nextInt(500)); // Số like ngẫu nhiên từ 0-499
            post.setCommentCount(random.nextInt(200)); // Số comment ngẫu nhiên từ 0-199

            posts.add(post);
        }
    }

}