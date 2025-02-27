package com.example.oruclejava.activity;

import android.content.Intent;
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

import com.example.oruclejava.R;
import com.example.oruclejava.api.ApiClient;
import com.example.oruclejava.api.AuthenticationApiService;
import com.example.oruclejava.api.mapper.request.RegisterRequest;
import com.example.oruclejava.api.mapper.response.RegisterResponse;
import com.example.oruclejava.utils.ClearFocus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextRePassword;
    private TextView textInButton, errorText;
    private ProgressBar progressBar;
    private CheckBox checkBoxAgree;
    private AuthenticationApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        initViews();
        setupListeners();
        apiService = ApiClient.getInstance().create(AuthenticationApiService.class);
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextRePassword = findViewById(R.id.edit_text_re_password);
        textInButton = findViewById(R.id.text_in_button);
        errorText = findViewById(R.id.errorText);
        progressBar = findViewById(R.id.progress_bar);
        checkBoxAgree = findViewById(R.id.checkbox_agree);
    }

    private void setupListeners() {
        findViewById(R.id.main).setOnClickListener(v ->
                ClearFocus.clearFocus(this, editTextEmail, editTextPassword, editTextRePassword));

        findViewById(R.id.button_back).setOnClickListener(v -> navigateTo(WelcomeActivity.class));
        findViewById(R.id.button_register_to_login).setOnClickListener(v -> navigateTo(LoginActivity.class));
        findViewById(R.id.button_register_to_app).setOnClickListener(v -> registerUser());
    }

    private void navigateTo(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
        finish();
    }

    private void registerUser() {
        ClearFocus.clearFocus(this, editTextEmail, editTextPassword, editTextRePassword);
        errorText.setVisibility(View.GONE);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editTextRePassword.getText().toString().trim();

        if (!validateInput(email, password, rePassword)) return;

        textInButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        apiService.register(new RegisterRequest(email, password)).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressBar.setVisibility(View.GONE);
                textInButton.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        navigateTo(SetupProfile.class);
                        finishAffinity();
                    } else {
                        showError(response.body().getMessage());
                    }
                } else {
                    showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                textInButton.setVisibility(View.VISIBLE);
                showError("Registration failed. Please try again.");
            }
        });
    }

    private boolean validateInput(String email, String password, String rePassword) {
        if (email.isEmpty()) {
            showError("Please enter email");
            editTextEmail.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            showError("Please enter password");
            editTextPassword.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return false;
        }
        if (!password.equals(rePassword)) {
            showError("Passwords do not match");
            return false;
        }
        if (!checkBoxAgree.isChecked()) {
            showError("Please agree to terms and conditions");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);
    }
}
