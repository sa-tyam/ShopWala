package com.officialshopwala.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
        final long productId = productItemList.get(position).getProductId();
        holder.productItemName.setText(productItemList.get(position).getName());
        holder.productItemPrice.setText("\u20B9" + Integer.toString(productItemList.get(position).getPrice()));
        holder.productItemMoreImageView.setImageResource(R.drawable.ic_edit_black);
        holder.productItemListedSwitch.setChecked(true);
        if( !productItemList.get(position).getProductImageUrl().equals("")) {
            Picasso.get()
                    .load(productItemList.get(position).getProductImageUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.productItemImageView);
        }
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
                Intent myIntent = new Intent(mContext, ProductDetailActivity.class);
                myIntent.putExtra("productId", productId);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(myIntent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, ProductDetailActivity.class);
                myIntent.putExtra("productId", productId);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(myIntent);
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

    public void searchData ( ArrayList<ProductItem> searchProductList) {
        productItemList = new ArrayList<>();
        productItemList.addAll(searchProductList);
        notifyDataSetChanged();
    }
}
