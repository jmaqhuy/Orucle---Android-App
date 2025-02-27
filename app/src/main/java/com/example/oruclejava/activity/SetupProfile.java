package com.example.oruclejava.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.oruclejava.MainActivity;
import com.example.oruclejava.R;
import com.example.oruclejava.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class SetupProfile extends AppCompatActivity {
    private RoundedImageView profileImage;
    private TextView textAddImage, textInButton, username;
    private String encodedImage;
    private FrameLayout profile_image_layout;

    private EditText name, dob;
    private ProgressBar progressBar;
    private int mYear, mMonth, mDay;

    private boolean buttonClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImage = findViewById(R.id.profile_image);
        textAddImage = findViewById(R.id.textAddImage);
        textInButton = findViewById(R.id.text_in_button);
        username = findViewById(R.id.displayName);
        name = findViewById(R.id.editText);
        dob = findViewById(R.id.editDOB);
        progressBar = findViewById(R.id.progress_bar);


        profile_image_layout = findViewById(R.id.profile_image_layout);
        profile_image_layout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        dob.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // Định dạng ngày tháng
                        String formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear);
                        dob.setText(formattedDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Gọi sau khi văn bản thay đổi
            }
        });


        findViewById(R.id.button_confirm).setOnClickListener(v -> {
            if (buttonClick) return;
            if (encodedImage == null){
                Toast.makeText(this, "Please select profile image", Toast.LENGTH_SHORT).show();
                return;
            } else if (name.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                return;
            } else if (dob.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
                return;
            }
            buttonClick = true;
            textInButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_NAME, name.getText().toString());
            user.put(Constants.KEY_DOB, dob.getText().toString());
            user.put(Constants.KEY_IMAGE, encodedImage);
            user.put(Constants.KEY_FOLLOWER, 0);
            user.put(Constants.KEY_FOLLOWING, 0);
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USER)
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .update(user)
                    .addOnSuccessListener(unused -> {
                        buttonClick = false;
                        textInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e ->
                    {
                        buttonClick = false;
                        textInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            profileImage.setImageBitmap(bitmap);
                            textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}