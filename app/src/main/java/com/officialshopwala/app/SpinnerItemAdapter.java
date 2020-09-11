package com.officialshopwala.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItems> {
    public SpinnerItemAdapter(@NonNull Context context, ArrayList<SpinnerItems> spinnerItemsArrayList) {
        super(context, 0 ,spinnerItemsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    public View initView (int position, View convertView, ViewGroup parent) {
        if ( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }
        TextView spinnerItemNameTextView = convertView.findViewById(R.id.spinnerItemName);

        SpinnerItems currentItem = getItem(position);

        if ( currentItem != null) {
            spinnerItemNameTextView.setText(currentItem.getSpinnerItemName());
        }

        return convertView;
    }
}
