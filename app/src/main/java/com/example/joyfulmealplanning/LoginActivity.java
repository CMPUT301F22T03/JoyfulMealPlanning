package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginEmail;
    TextInputEditText loginPassword;
    TextView Registration;
    Button loginButton;

    FirebaseAuth joyfulMealPlanningAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        loginEmail = findViewById(R.id.userEmailInput);
        loginPassword = findViewById(R.id.userPasswordInput);
        Registration = findViewById(R.id.registration);
        loginButton = findViewById(R.id.login_button);

        joyfulMealPlanningAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            userLogin();
        });
        Registration.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void userLogin() {
        String email = loginEmail.getText().toString();
        String psw = loginPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Email cannot be empty.");
            loginEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(psw)) {
            loginPassword.setError("Password cannot be empty.");
            loginPassword.requestFocus();
        }
        else {
            joyfulMealPlanningAuth.signInWithEmailAndPassword(email, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User is logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}