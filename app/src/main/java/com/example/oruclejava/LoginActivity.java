package com.example.oruclejava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
import com.example.oruclejava.utils.Constants;
import com.example.oruclejava.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private CheckBox checkBoxRememberMe;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private TextView textInButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.main).setOnClickListener(v -> {
            ClearFocus.clearFocus(this, editTextEmail, editTextPassword);
        });

        mAuth = FirebaseAuth.getInstance();
        checkBoxRememberMe = findViewById(R.id.checkbox_remember);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textInButton = findViewById(R.id.text_in_button);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.button_back).setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });

        findViewById(R.id.button_login_to_register).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        findViewById(R.id.button_login_to_app).setOnClickListener(v -> Login());
    }

    private void Login() {


        ClearFocus.clearFocus(this,editTextEmail, editTextPassword);
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USER)
                        .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists() && documentSnapshot.contains(Constants.KEY_NAME)) {
                                if (checkBoxRememberMe.isChecked()) {
                                    PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                                    preferenceManager.putBoolean(Constants.KEY_REMEMBER_ME, true);
                                }
                                startActivity(new Intent(this, MainActivity.class));
                            } else {
                                startActivity(new Intent(this, SetupProfile.class));
                            }
                            finishAffinity();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Lỗi khi lấy dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                        });
            } else {
                textInButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error! " +
                        Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}