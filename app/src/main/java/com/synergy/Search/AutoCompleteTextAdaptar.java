package com.synergy.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.synergy.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AutoCompleteTextAdaptar extends ArrayAdapter<StatusItem> {

    private List<StatusItem> items;
    String role;
    private int mLastClicked;

    public void setLastClicked(int lastClicked) {
        mLastClicked = lastClicked;
    }

    public AutoCompleteTextAdaptar(@NonNull Context context, @NonNull List<StatusItem> items, String role) {
        super(context, 0, items);
        this.items = items;
        this.role = role;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.custom_layout_autocomplete_textview, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.status_text_layout);
        StatusItem statusItem = getItem(position);
        if (statusItem != null) {
            textView.setText(statusItem.getStatus());
        }
        Log.d(TAG, "getView: to rol" + role);
        if (role.equals("Technician")) {
            String textviewText = textView.getText().toString();
            convertView.setEnabled(!textviewText.equals("Pause"));

        }


        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

        if (role.equals("Technician")) {

            return position != 1;
        }

        return true;


    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}