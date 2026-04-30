package com.example.javaai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPageActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private boolean isPasswordVisible = false;
    private EditText emailField, passwordField;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        ImageView passwordIcon = findViewById(R.id.password_icon);
        rememberMeCheckBox = findViewById(R.id.remember_me);
        Button loginButton = findViewById(R.id.login4);
        ImageView googleSignInButton = findViewById(R.id.google); // Google Sign-In button

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your Web Client ID
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Autofill email from Signup
        autofillEmailFromSignup();

        Button signButton = findViewById(R.id.signup2);
        signButton.setOnClickListener(v -> {
            Intent login = new Intent(LoginPageActivity.this, SignupPageActivity.class);
            startActivity(login);
        });
        Button forgotButton = findViewById(R.id.forget_button);
        forgotButton.setOnClickListener(v -> {
            Intent login = new Intent(LoginPageActivity.this, ForgotPasswordActivity.class);
            startActivity(login);
        });

        // Login Button Click
        loginButton.setOnClickListener(v -> {
            String emailInput = emailField.getText().toString().trim().toLowerCase(); // Convert to lowercase
            String passwordInput = passwordField.getText().toString().trim();

            if (validateCredentials(emailInput, passwordInput)) {
                Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Save credentials if Remember Me is checked
                if (rememberMeCheckBox.isChecked()) {
                    saveCredentials(emailInput, passwordInput);
                }

                Intent intent = new Intent(LoginPageActivity.this, SignupPageActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginPageActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Password Visibility Toggle
        passwordIcon.setOnClickListener(v -> togglePasswordVisibility());

        // Google Sign-In Button Click
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Google sign-in failed", e);
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Welcome, " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                        // Redirect to another activity
                        Intent intent = new Intent(LoginPageActivity.this, ListFragment.class); // Replace with your main activity
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Validate Credentials from SharedPreferences
    private boolean validateCredentials(String email, String password) {
        String users = sharedPreferences.getString("users", "{}");

        try {
            JSONObject usersJson = new JSONObject(users);
            if (usersJson.has(email)) {
                String savedPassword = usersJson.getString(email);
                return savedPassword.equals(password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Autofill Email from Signup Page
    private void autofillEmailFromSignup() {
        String savedEmail = sharedPreferences.getString("signup_email", "");
        if (!savedEmail.isEmpty()) {
            emailField.setText(savedEmail);
        }
    }

    // Save Credentials for "Remember Me"
    private void saveCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_email", email);
        editor.putString("saved_password", password);
        editor.putBoolean("remember_me", true);
        editor.apply();
    }

    // Toggle Password Visibility
    private void togglePasswordVisibility() {
        passwordField.setInputType(
                isPasswordVisible ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                        : InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        );
        isPasswordVisible = !isPasswordVisible;
        passwordField.setSelection(passwordField.length());
    }
}
