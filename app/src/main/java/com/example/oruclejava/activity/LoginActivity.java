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

import com.example.oruclejava.MainActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.api.ApiClient;
import com.example.oruclejava.api.AuthenticationApiService;
import com.example.oruclejava.api.mapper.request.LoginRequest;
import com.example.oruclejava.api.mapper.response.LoginResponse;
import com.example.oruclejava.utils.ClearFocus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private TextView textInButton, errorText;
    private ProgressBar progressBar;
    private AuthenticationApiService apiService;
    private boolean isClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = ApiClient.getInstance().create(AuthenticationApiService.class);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textInButton = findViewById(R.id.text_in_button);
        progressBar = findViewById(R.id.progress_bar);
        errorText = findViewById(R.id.errorText);
    }

    private void setupClickListeners() {
        findViewById(R.id.main).setOnClickListener(v ->
                ClearFocus.clearFocus(this, editTextEmail, editTextPassword));

        findViewById(R.id.button_back).setOnClickListener(v -> navigateTo(WelcomeActivity.class));
        findViewById(R.id.button_login_to_register).setOnClickListener(v -> navigateTo(RegisterActivity.class));
        findViewById(R.id.button_login_to_app).setOnClickListener(v -> login());
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
        finishAffinity();
    }

    private boolean validateInputs() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            showError("Please enter email", editTextEmail);
            return false;
        }
        if (password.isEmpty()) {
            showError("Please enter password", editTextPassword);
            return false;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters", editTextPassword);
            return false;
        }
        return true;
    }

    private void showError(String message, EditText targetField) {
        errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);
        targetField.requestFocus();
    }

    private void login() {
        if (!isClickable || !validateInputs()) return;

        errorText.setVisibility(View.GONE);
        textInButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        isClickable = false;

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        apiService.authenticate(LoginRequest.builder().email(email).password(password).build())
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        resetUI();
                        if (!response.isSuccessful() || response.body() == null) {
                            showError("An error occurred", editTextEmail);
                            return;
                        }

                        LoginResponse loginResponse = response.body();
                        if (!loginResponse.isSuccess()) {
                            showError(loginResponse.getMessage(), editTextEmail);
                            return;
                        }

                        if (loginResponse.isUpdateProfile()) {
                            navigateTo(SetupProfile.class);
                        } else {
                            navigateTo(MainActivity.class);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        resetUI();
                        showError("Network error. Please try again.", editTextEmail);
                    }
                });
    }

    private void resetUI() {
        textInButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        isClickable = true;
    }
}
