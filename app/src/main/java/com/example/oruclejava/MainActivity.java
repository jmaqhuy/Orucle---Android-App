package com.example.oruclejava;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oruclejava.activity.LogoutBottomSheet;
import com.example.oruclejava.activity.SearchActivity;
import com.example.oruclejava.activity.WelcomeActivity;
import com.example.oruclejava.adapter.ViewPagerAdapter;
import com.example.oruclejava.fragment.HomeFragment;
import com.example.oruclejava.fragment.MessageFragment;
import com.example.oruclejava.fragment.NotificationFragment;
import com.example.oruclejava.fragment.ProfileFragment;
import com.example.oruclejava.utils.Constants;
import com.example.oruclejava.utils.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_nav;
    private ViewPager2 viewpager;
    private Toolbar toolbar;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private FirebaseFirestore database;
    private DocumentSnapshot currentUserDoc;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        database = FirebaseFirestore.getInstance();
        getUserDocumentSnapshot();


        bottom_nav = findViewById(R.id.main_bottom_nav);
        viewpager = findViewById(R.id.main_viewpager);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        bottom_nav.findViewById(R.id.profile).setOnLongClickListener(v -> {
            LogoutBottomSheet bottomSheet = new LogoutBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "LogoutBottomSheet");
            return true;
        });

    }
    private void setViewpagerAndBottomNav(){
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        fragments.add(homeFragment);
        fragments.add(new MessageFragment());
        fragments.add(new NotificationFragment());
        fragments.add(profileFragment);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragments);
        viewpager.setAdapter(adapter);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottom_nav.getMenu().getItem(position).setChecked(true);
                updateToolbarTitle(position);
            }
        });
        viewpager.setOffscreenPageLimit(4);

        bottom_nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            int target;
            if (id == R.id.home) {
                target = 0;
                if (viewpager.getCurrentItem() == target){
                    if (homeFragment != null) {
                        homeFragment.scrollToTop();
                    }
                }
            } else if (id == R.id.message) {
                target = 1;
            } else if (id == R.id.noti) {
                target = 2;
            } else if (id == R.id.profile) {
                target = 3;
                if (viewpager.getCurrentItem() == target){
                    if (profileFragment != null) {
                        profileFragment.scrollToTop();
                    }
                }
            } else {
                target = 0;
            }
            viewpager.setCurrentItem(target, false);
            updateToolbarTitle(target);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateToolbarTitle(int position){
        if (position == 0){
            toolbar.setTitle("Feeds");
        } else if (position == 1){
            toolbar.setTitle("Messages");
        } else if (position == 2){
            toolbar.setTitle("Notifications");
        } else if (position == 3){
            toolbar.setTitle("Profile");
        } else {
            toolbar.setTitle("Feeds");
        }
    }
    public void switchToProfileFragment() {
        viewpager.setCurrentItem(3, false);
    }

    private void getUserDocumentSnapshot(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        database.collection(Constants.KEY_COLLECTION_USER)
                .document(Objects.requireNonNull(userId))
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()){
                        currentUserDoc = document;
                        setViewpagerAndBottomNav();
                        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                        preferenceManager.putString(Constants.KEY_SENDER_NAME, currentUserDoc.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_SENDER_IMAGE, currentUserDoc.getString(Constants.KEY_IMAGE));
                    } else {
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });

    }

    public void getAvatar(RoundedImageView userAvatar){
        if (currentUserDoc == null) return;
        String encodedImage = currentUserDoc.getString(Constants.KEY_IMAGE);
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            userAvatar.setImageBitmap(
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            Log.d("HOME FRAGMENT", "set image success");
        } else {
            Log.d("HOME FRAGMENT", "Image data is null");
        }
    }

    public String getUsername(){
        if (currentUserDoc == null) return "";
        return currentUserDoc.getString((Constants.KEY_NAME));
    }

}