package com.example.javaai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyListAdapter extends BaseAdapter {

    private Context context;
    private List<String> itemList;
    private String listName;
    private static final String PREF_NAME = "list_preferences";
    private final SharedPreferences sharedPreferences;
    private final Set<String> checkedItems;
    private final Handler handler = new Handler(); // For delayed updates

    public MyListAdapter(Context context, List<String> itemList, String listName) {
        this.context = context;
        this.itemList = itemList;
        this.listName = listName;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.checkedItems = new HashSet<>(sharedPreferences.getStringSet(listName + "_checked_items", new HashSet<>()));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView itemNameTextView = convertView.findViewById(R.id.item_name);
        CheckBox checkBox = convertView.findViewById(R.id.check_box);

        String itemName = itemList.get(position);
        itemNameTextView.setText(itemName);
        checkBox.setChecked(checkedItems.contains(itemName));

        // Remove previous listener before setting a new one to avoid redundant calls
        checkBox.setOnCheckedChangeListener(null);

        checkBox.setChecked(checkedItems.contains(itemName));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedItems.add(itemName);
            } else {
                checkedItems.remove(itemName);
            }

            // Delayed update to batch multiple changes
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(listName + "_checked_items", new HashSet<>(checkedItems));
                editor.apply();
            }, 300); // Wait 300ms before updating to handle multiple changes together
        });

        return convertView;
    }
}
