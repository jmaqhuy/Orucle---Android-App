package com.example.oruclejava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oruclejava.adapter.AdapterSearchUserResult;
import com.example.oruclejava.models.UserModel;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<UserModel> users;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.button_back).setOnClickListener(v -> {
            finish();
        });
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.result);
        progressBar = findViewById(R.id.progressBar);
        loading(true);
        getUser();



    }

    private void getUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = Objects.requireNonNull(
                            FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    if (task.isSuccessful() && task.getResult() != null) {
                        users.clear();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            String userId = queryDocumentSnapshot.getId();
                            String username = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            String image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            if (username == null || image == null || username.isEmpty() || image.isEmpty()) continue;
                            UserModel userModel = new UserModel(
                                    userId,
                                    image,
                                    username,
                                    false);
                            users.add(userModel);
                        }
                        if (!users.isEmpty()) {
                            AdapterSearchUserResult adapterSearchUserResult = new AdapterSearchUserResult(users);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapterSearchUserResult);
                            loading(false);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void loading(boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}