package com.example.joyfulmealplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText registrationEmail;
    TextInputEditText registrationPassword;
    TextView Cancel;
    Button registrationButton;

    FirebaseAuth joyfulMealPlanningAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registrationEmail = findViewById(R.id.user_email_registration);
        registrationPassword = findViewById(R.id.psw_registration);
        Cancel = findViewById(R.id.cancel);
        registrationButton = findViewById(R.id.registration_button);

        joyfulMealPlanningAuth = FirebaseAuth.getInstance();

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void createUser() {
        String email = registrationEmail.getText().toString();
        String psw = registrationPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            registrationEmail.setError("Email cannot be empty.");
            registrationEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(psw)) {
            registrationPassword.setError("Password cannot be empty.");
            registrationPassword.requestFocus();

        }
        else {
            joyfulMealPlanningAuth.createUserWithEmailAndPassword(email, psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "Registration error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}