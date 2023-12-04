package com.chedly.project;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.chedly.project.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginBtn , rd;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginButton);
        //signupText = findViewById(R.id.signupText);
        rd = findViewById(R.id.rd);
        mAuth = FirebaseAuth.getInstance();

        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });


        /*signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirectToRegisterPage= new Intent(getApplicationContext(),Login.class);
                startActivity(redirectToRegisterPage);
                finish();
            }
        });*/


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText, passwordText;

                emailText = email.getText().toString();
                passwordText = password.getText().toString();

                if (emailText.isEmpty()) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (passwordText.isEmpty()) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent redirectToHomePage=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(redirectToHomePage);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}