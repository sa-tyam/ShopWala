package com.officialshopwala.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context mContext;
    ArrayList<OrderItem> orderItemArrayList;

    public OrderAdapter (Context mContext, ArrayList<OrderItem> orderItemArrayList) {
        this.mContext = mContext;
        this.orderItemArrayList = orderItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.orderNumberTitleTextView.setText("Order #"+orderItemArrayList.get(position).getOrderNumber());
        holder.orderPriceTextView.setText("$"+orderItemArrayList.get(position).getPrice());
        holder.orderItemCountTextView.setText(orderItemArrayList.get(position).getItemCount()+ " items");
        holder.orderItemTimeTextView.setText(orderItemArrayList.get(position).getOrderTime());
        holder.orderStatusTextView.setText(orderItemArrayList.get(position).getOrderStatus());
        holder.orderDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTitleTextView;
        TextView orderPriceTextView;
        TextView orderItemCountTextView;
        TextView orderItemTimeTextView;
        TextView orderStatusTextView;
        Button orderDetailButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderNumberTitleTextView = itemView.findViewById(R.id.orderNumberTitleTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            orderItemCountTextView = itemView.findViewById(R.id.orderItemCountTextView);
            orderItemTimeTextView = itemView.findViewById(R.id.orderItemTimeTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderDetailButton = itemView.findViewById(R.id.orderDetailButton);

        }
    }
}
