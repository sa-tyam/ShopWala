package com.officialshopwala.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context mContext;
    static ArrayList<ProductItem> productItemList;

    public ProductAdapter (Context mContext, ArrayList<ProductItem> productItemList) {
        this.mContext = mContext;
        this.productItemList = productItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.productItemName.setText(productItemList.get(position).getName());
        holder.productItemPrice.setText("$" + Integer.toString(productItemList.get(position).getPrice()));
        holder.productItemListedSwitch.setChecked(true);
        holder.productItemListedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.productItemListedSwitch.isChecked()) {
                    holder.productItemListedOrNot.setText("Listed Online");
                } else {
                    holder.productItemListedOrNot.setText("Not Listed");
                }
            }
        });
        holder.productItemMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productItemName;
        TextView productItemPrice;
        TextView productItemListedOrNot;
        Switch productItemListedSwitch;
        ImageView productItemImageView;
        ImageView productItemMoreImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productItemName = itemView.findViewById(R.id.productItemName);
            productItemPrice = itemView.findViewById(R.id.productItemPriceTextView);
            productItemListedOrNot = itemView.findViewById(R.id.productItemSwitchTextView);
            productItemListedSwitch = itemView.findViewById(R.id.productItemListedOnlineSwitch);
            productItemImageView = itemView.findViewById(R.id.productItemImageView);
            productItemMoreImageView = itemView.findViewById(R.id.productItemMoreButtonImage);
        }
    }
}
