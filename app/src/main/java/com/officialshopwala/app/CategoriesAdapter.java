package com.officialshopwala.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        final String categoryName = categoryArrayList.get(position).getCategoryName();
        holder.categoryNameTextView.setText(categoryName);
        holder.categoryItemCountTextView.setText(Integer.toString(categoryArrayList.get(position).getNumberOfProducts()) + " Product listed");
        holder.categoryItemMoreImage.setImageResource(R.drawable.ic_edit_black);

//        Glide.with(holder.categoryItemMoreImage).asDrawable()
//                .load(R.drawable.ic_more_black)
//                .into(holder.categoryItemMoreImage);


        holder.categoryItemMoreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, CategoryDetailActivity.class);
                myIntent.putExtra("categoryName" , categoryName);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(myIntent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, ProductsActivity.class);
                myIntent.putExtra("categoryName" , categoryName);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(myIntent);
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

            categoryNameTextView = itemView.findViewById(R.id.productItemName);
            categoryItemCountTextView = itemView.findViewById(R.id.productItemSwitchTextView);
            categoryItemMoreImage = itemView.findViewById(R.id.categoryItemMoreImageView);

        }
    }
}
