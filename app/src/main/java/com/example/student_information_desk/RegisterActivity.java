package com.example.student_information_desk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText studentIdEditText;
    private EditText nameEditText;
    private EditText studentEmailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private Button loginButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        studentIdEditText = findViewById(R.id.studentIdEditText);
        nameEditText = findViewById(R.id.nameEditText);
        studentEmailEditText = findViewById(R.id.studentEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Set up register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set up login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser() {
        String studentId = studentIdEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String studentEmail = studentEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input fields
        if (studentId.isEmpty() || name.isEmpty() || studentEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the student ID is a 7-digit number
        if (!isValidStudentId(studentId)) {
            Toast.makeText(RegisterActivity.this, "Invalid student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with email and password
        firebaseAuth.createUserWithEmailAndPassword(studentEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Registration success, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            // Redirect to the login activity or another screen
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean isValidStudentId(String studentId) {
        // Check if the student ID is a 7-digit number
        return studentId.matches("\\d{7}");
    }
}
