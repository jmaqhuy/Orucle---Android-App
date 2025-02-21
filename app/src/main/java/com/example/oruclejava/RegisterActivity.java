package com.example.oruclejava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oruclejava.utils.ClearFocus;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword;
    private TextView textInButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textInButton = findViewById(R.id.text_in_button);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.main).setOnClickListener(v -> {
            ClearFocus.clearFocus(this,editTextName, editTextEmail, editTextPassword);
        });


        findViewById(R.id.button_back).setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        findViewById(R.id.button_register_to_login).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.button_register_to_app).setOnClickListener( v -> {
            ClearFocus.clearFocus(this,editTextName, editTextEmail, editTextPassword);
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (email.isEmpty()){
                editTextEmail.setError("Please enter email");
                editTextEmail.requestFocus();
                return;
            } else if (password.isEmpty()){
                editTextPassword.setError("Please enter password");
                editTextPassword.requestFocus();
                return;
            }
            if (password.length() < 6){
                editTextPassword.setError("Password must be at least 6 characters");
                return;
            }
            textInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                } else {
                    textInButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}