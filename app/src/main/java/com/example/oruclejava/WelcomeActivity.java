package com.example.oruclejava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences preferences = getSharedPreferences("OruclePrefs", MODE_PRIVATE);
        boolean rememberMe = preferences.getBoolean("remember_me", false);

        if (!rememberMe) {
            FirebaseAuth.getInstance().signOut();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        findViewById(R.id.button_login).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));

        });

        findViewById(R.id.button_register).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));

        });
    }
}