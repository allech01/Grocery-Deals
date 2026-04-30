package com.example.javaai;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javaai.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Remove icon tint if necessary
        binding.b1.setItemIconTintList(null);

        // Check if the activity is being recreated (e.g., on configuration changes)
        if (savedInstanceState == null) {
            replaceFragment(new ListFragment());  // Default fragment
        }

        // Set up listener for the bottom navigation
        binding.b1.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                // Check which item was selected and replace the fragment accordingly
                if (itemId == R.id.list) {
                    fragment = new ListFragment();
                } else if (itemId == R.id.profile) {
                    fragment = new AiFragment();  // Show AiFragment within the same activity
                } else if (itemId == R.id.setting) {
                    fragment = new SettingFragment();
                }

                // If a fragment was selected, replace it
                if (fragment != null) {
                    replaceFragment(fragment);
                }
                return true;
            }
        });
    }

    // Method to replace the current fragment with a new one
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);  // Replace fragment in the container
        fragmentTransaction.addToBackStack(null);  // Optional: Add to back stack
        fragmentTransaction.commit();
    }
}
