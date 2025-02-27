package com.example.oruclejava.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oruclejava.R;
import com.example.oruclejava.utils.Constants;
import com.example.oruclejava.utils.PreferenceManager;

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
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        boolean rememberMe = preferenceManager.getBoolean(Constants.KEY_REMEMBER_ME);



        findViewById(R.id.button_login).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));

        });

        findViewById(R.id.button_register).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));

        });
    }
}