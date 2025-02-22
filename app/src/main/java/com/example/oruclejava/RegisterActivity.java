package com.example.oruclejava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextRePassword;
    private TextView textInButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private CheckBox checkBoxAgree;

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
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextRePassword = findViewById(R.id.edit_text_re_password);
        textInButton = findViewById(R.id.text_in_button);
        checkBoxAgree = findViewById(R.id.checkbox_agree);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.main).setOnClickListener(v -> {
            ClearFocus.clearFocus(this,editTextRePassword, editTextEmail, editTextPassword);
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
            ClearFocus.clearFocus(this,editTextRePassword, editTextEmail, editTextPassword);
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (email.isEmpty()){
                editTextEmail.setError("Please enter email");
                editTextEmail.requestFocus();
                return;
            } else if (password.isEmpty()){
                editTextPassword.setError("Please enter password");
                editTextPassword.requestFocus();
                return;
            } else if (!password.equals(editTextRePassword.getText().toString())){
                editTextRePassword.setError("Password does not match");
                editTextRePassword.requestFocus();
                return;
            }
            if (password.length() < 6){
                editTextPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!checkBoxAgree.isChecked()){
                Toast.makeText(this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
                return;
            }
            textInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    HashMap<String, Object> user = new HashMap<>();
                    user.put(Constants.KEY_EMAIL, Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                    database.collection(Constants.KEY_COLLECTION_USER)
                            .document(mAuth.getCurrentUser().getUid())
                            .set(user)
                                    .addOnSuccessListener(documentReference -> {})
                                            .addOnFailureListener(exception -> {});

                    startActivity(new Intent(this, SetupProfile.class));
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