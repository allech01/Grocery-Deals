package com.example.javaai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemsActivity extends AppCompatActivity {

    private ListView listView;
    private MyListAdapter adapter;
    private List<String> itemList;
    private String listName;
    private EditText itemInput;
    private Button addButton;
    private static final String PREF_NAME = "list_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        listView = findViewById(R.id.listView1);
        itemInput = findViewById(R.id.itemInput1);
        addButton = findViewById(R.id.addButton1);

        // Get the list name from intent
        listName = getIntent().getStringExtra("list_name");

        if (listName == null) {
            Toast.makeText(this, "Error: No list name provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load saved items
        itemList = loadItemsFromSharedPreferences();

        // Initialize adapter
        adapter = new MyListAdapter(this, itemList, listName);
        listView.setAdapter(adapter);

        // Handle Add Button Click
        addButton.setOnClickListener(v -> addItem());
    }

    // Load items from SharedPreferences
    private List<String> loadItemsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Set<String> savedItems = sharedPreferences.getStringSet(listName, new HashSet<>());

        return new ArrayList<>(savedItems);
    }

    // Add item to the list
    private void addItem() {
        String newItem = itemInput.getText().toString().trim();

        if (newItem.isEmpty()) {
            Toast.makeText(this, "Enter an item name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemList.contains(newItem)) {
            Toast.makeText(this, "Item already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        itemList.add(newItem);

        // Save updated list in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> savedItems = new HashSet<>(itemList);
        editor.putStringSet(listName, savedItems);
        editor.apply();

        // Refresh UI
        adapter.notifyDataSetChanged();
        itemInput.setText(""); // Clear input field
    }
}
