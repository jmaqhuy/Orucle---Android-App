package com.example.oruclejava;

import static java.lang.Math.abs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oruclejava.adapter.ViewPagerAdapter;
import com.example.oruclejava.fragment.HomeFragment;
import com.example.oruclejava.fragment.MessageFragment;
import com.example.oruclejava.fragment.NotificationFragment;
import com.example.oruclejava.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottom_nav;
    private ViewPager2 viewpager;
    private Toolbar toolbar;
    private ArrayList<Fragment> fragments = new ArrayList<>();

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

        bottom_nav = findViewById(R.id.main_bottom_nav);
        viewpager = findViewById(R.id.main_viewpager);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new NotificationFragment());
        fragments.add(new ProfileFragment());

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

        bottom_nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            int target;
            if (id == R.id.home) {
                target = 0;
            } else if (id == R.id.message) {
                target = 1;
            } else if (id == R.id.noti) {
                target = 2;
            } else if (id == R.id.profile) {
                target = 3;
            } else {
                target = 0;
            }
            viewpager.setCurrentItem(target);
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

}