package com.example.javaai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private String generatedOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText emailField = findViewById(R.id.email);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();

                if (!email.isEmpty() && isValidEmail(email)) {
                    generatedOtp = generateOtp();  // Generate OTP
                   // sendOtpToEmail(email, generatedOtp);  // Send OTP to email

                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to your email", Toast.LENGTH_SHORT).show();

                    // Navigate to OTP Verification Fragment
                    OtpVerificationActivity otpVerificationFragment = new OtpVerificationActivity();

                    // Pass OTP and Email to the fragment using a Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("otp", generatedOtp);
                    otpVerificationFragment.setArguments(bundle);

                    // Use FragmentTransaction to replace the current fragment with OtpVerificationFragment
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, otpVerificationFragment);
                    transaction.addToBackStack(null);  // Optional: Allows you to go back
                    transaction.commit();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.signup3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, SignupPageActivity.class);
                startActivity(intent);
            }
        });
    }

    // Generate a 4-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000); // Generate 4-digit OTP
        return String.valueOf(otp);
    }

    // Validate if the email is in correct format
    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
    // Send OTP to email using GMailSender
   /* private void sendOtpToEmail(String email, String otp) {
        new Thread(() -> {
            try {
                // Replace with your Gmail address and App Password
                GMailSender sender = new GMailSender("your-email@gmail.com", "your-app-password");  // Your email credentials
                sender.sendMail(
                        "Password Reset OTP",
                        "Your OTP for password reset is: " + otp,
                        "your-email@gmail.com", // Sender email
                        email // Recipient email
                );
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Failed to send OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
*/