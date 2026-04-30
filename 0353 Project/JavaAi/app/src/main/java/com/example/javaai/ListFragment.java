package com.example.javaai;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<String> dataList;
    private static final String PREF_NAME = "list_preferences";

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Button button = view.findViewById(R.id.button_new_list);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateList.class);
            startActivity(intent);
        });

        recyclerView = view.findViewById(R.id.receylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataList = new ArrayList<>();
        loadListNamesFromSharedPreferences();

        myAdapter = new MyAdapter(getActivity(), dataList, this);
        recyclerView.setAdapter(myAdapter);

        return view;
    }

    private void loadListNamesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
        Set<String> savedLists = sharedPreferences.getStringSet("list_names", new HashSet<>());

        dataList.clear();
        if (savedLists != null) {
            dataList.addAll(savedLists);
        }
    }

    public void deleteList(String listName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove the list name from SharedPreferences
        Set<String> savedLists = sharedPreferences.getStringSet("list_names", new HashSet<>());
        if (savedLists != null) {
            savedLists.remove(listName);
            editor.putStringSet("list_names", savedLists);
        }

        // Remove the associated list items
        editor.remove(listName);
        editor.apply();

        // Refresh the list
        loadListNamesFromSharedPreferences();
        myAdapter.notifyDataSetChanged();
    }
}
