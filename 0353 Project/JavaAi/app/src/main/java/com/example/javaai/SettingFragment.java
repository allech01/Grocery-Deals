package com.example.javaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class SettingFragment extends Fragment {

    private AdView bannerAdView;
    private InterstitialAd interstitialAd;
    private GoogleSignInClient mGoogleSignInClient; // Google Sign-In Client
    private FirebaseAuth mAuth;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your Firebase Web Client ID
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Initialize AdMob
        MobileAds.initialize(requireActivity(), initializationStatus -> {});

        // Add your device as a test device
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Collections.singletonList("YOUR_DEVICE_ID")) // Replace YOUR_DEVICE_ID
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        // Load Banner Ad
        bannerAdView = view.findViewById(R.id.banner_ad_view1);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        // Load Interstitial Ad
        loadInterstitialAd();

        // Login button setup
        Button loginButton = view.findViewById(R.id.button_login1);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), login_main.class); // Replace login_main with your Activity name
            startActivity(intent);
        });

        // Show Ad button setup
        Button showAdButton = view.findViewById(R.id.btn_show1);
        showAdButton.setOnClickListener(v -> {
            if (interstitialAd != null) {
                interstitialAd.show(requireActivity());
            } else {
                Toast.makeText(getActivity(), "Ad not loaded yet. Please wait.", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout button setup
        Button logoutButton = view.findViewById(R.id.button_logout1);
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireActivity(), getString(R.string.admob_interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        Toast.makeText(getActivity(), "Interstitial Ad Loaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        interstitialAd = null;
                        Toast.makeText(getActivity(), "Failed to Load Ad: " + adError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logout() {
        // Sign out from Google
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                // Sign out from Firebase
                mAuth.signOut();

                // Show a toast and redirect to login
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), login_main.class); // Replace login_main with your login activity
                startActivity(intent);
                requireActivity().finish(); // Close the current activity
            });
        } else {
            // If GoogleSignInClient is not initialized, just sign out from Firebase
            mAuth.signOut();
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), login_main.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }
}
