package com.chedly.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText email, password;
    Button registerBtn;
    FirebaseAuth mAuth;
    TextView signinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerButton);
        signinText = findViewById(R.id.signinText);
        mAuth = FirebaseAuth.getInstance();



        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirectToLoginPage= new Intent(getApplicationContext(),Login.class);
                startActivity(redirectToLoginPage);

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText, passwordText;

                emailText = email.getText().toString();
                passwordText = password.getText().toString();
                if (emailText.isEmpty()) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (passwordText.isEmpty()) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // You can add further actions here after successful registration
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
