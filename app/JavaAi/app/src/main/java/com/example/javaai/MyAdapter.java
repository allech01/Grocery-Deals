package com.example.javaai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<String> dataList;
    private ListFragment fragment;

    public MyAdapter(Context context, List<String> dataList, ListFragment fragment) {
        this.context = context;
        this.dataList = dataList;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String listName = dataList.get(position);
        holder.listNameTextView.setText(listName);

        // Handle list item click to open ItemsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemsActivity.class);
            intent.putExtra("list_name", listName);
            context.startActivity(intent);
        });

        // Handle delete option button click
        holder.optionsButton.setOnClickListener(v -> showDeleteConfirmationDialog(listName, position));
    }

    private void showDeleteConfirmationDialog(String listName, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete List")
                .setMessage("Are you sure you want to delete this list?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Remove the list from SharedPreferences
                    fragment.deleteList(listName);

                    // Ensure position is valid before removing
                    if (position >= 0 && position < dataList.size()) {
                        dataList.remove(position);
                        notifyDataSetChanged(); // Refresh RecyclerView properly
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView listNameTextView;
        ImageButton optionsButton;

        public ViewHolder(View itemView) {
            super(itemView);
            listNameTextView = itemView.findViewById(R.id.list_name1);
            optionsButton = itemView.findViewById(R.id.options_button1);
        }
    }
}
