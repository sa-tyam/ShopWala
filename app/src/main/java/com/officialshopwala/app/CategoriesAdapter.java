package com.officialshopwala.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Category> categoryArrayList;

    public CategoriesAdapter(Context mContext, ArrayList<Category> categoryArrayList) {
        this.mContext = mContext;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryNameTextView.setText(categoryArrayList.get(position).getCategoryName());
        holder.categoryItemCountTextView.setText(categoryArrayList.get(position).getNumberOfProducts() + " Product listed");
        holder.categoryItemMoreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("category more", "clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        TextView categoryItemCountTextView;
        ImageView categoryItemMoreImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryItemCountTextView = itemView.findViewById(R.id.categoryItemCountTextView);
            categoryItemMoreImage = itemView.findViewById(R.id.categoryItemMoreImageView);

        }
    }
}
