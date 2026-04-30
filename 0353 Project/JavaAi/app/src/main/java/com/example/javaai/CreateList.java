package com.example.javaai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateList extends AppCompatActivity {

    private EditText editTextListName, editTextItem;
    private Button buttonAddItem, buttonSaveList;
    private TextView textViewItems;
    private List<String> items;
    private static final String PREF_NAME = "list_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        // Initialize UI components
        editTextListName = findViewById(R.id.edit_text_list_name);
        editTextItem = findViewById(R.id.edit_text_item);
        buttonAddItem = findViewById(R.id.button_add_item);
        buttonSaveList = findViewById(R.id.button_save_list);
        textViewItems = findViewById(R.id.text_view_items);

        // Initialize the list of items
        items = new ArrayList<>();

        // Button to add item to the list
        buttonAddItem.setOnClickListener(v -> {
            String item = editTextItem.getText().toString();
            if (!item.isEmpty()) {
                items.add(item);
                updateItemListDisplay();
                editTextItem.setText(""); // Clear item input
            } else {
                Toast.makeText(CreateList.this, "Please enter an item", Toast.LENGTH_SHORT).show();
            }
        });

        // Button to save the list to SharedPreferences
        buttonSaveList.setOnClickListener(v -> {
            String listName = editTextListName.getText().toString();
            if (!listName.isEmpty() && !items.isEmpty()) {
                saveListToSharedPreferences(listName, items);
                Toast.makeText(CreateList.this, "List saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateList.this, "Please enter a valid list name and items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the TextView with the list of items
    private void updateItemListDisplay() {
        StringBuilder itemListText = new StringBuilder();
        for (String item : items) {
            itemListText.append(item).append("\n");
        }
        textViewItems.setText(itemListText.toString());
    }

    // Save the list name and items to SharedPreferences
    // Save the list name and items to SharedPreferences
    private void saveListToSharedPreferences(String listName, List<String> items) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the current lists and add the new one
        Set<String> savedLists = sharedPreferences.getStringSet("list_names", new HashSet<>());
        savedLists.add(listName);
        editor.putStringSet("list_names", savedLists);

        // Save the items for this list
        editor.putStringSet(listName, new HashSet<>(items));
        editor.apply();
    }

}
