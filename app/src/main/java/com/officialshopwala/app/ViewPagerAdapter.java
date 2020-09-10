package com.officialshopwala.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    ArrayList<OnboardItem> onboardItems;
    Context mContext;

    public ViewPagerAdapter ( Context mContext, ArrayList<OnboardItem> onboardItems) {
        this.mContext = mContext;
        this.onboardItems = onboardItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.onboard_items_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onboardImageView.setImageResource(onboardItems.get(position).getOnboardImage());
        holder.titleTextView.setText(onboardItems.get(position).getTitle());
        holder.descriptionTextView.setText(onboardItems.get(position).getDescription());
    }

    public int getChildNumber () {
        return 1;
    }

    @Override
    public int getItemCount() {
        return onboardItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView onboardImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.onBoardTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.onBoardDescriptionTextView);
            onboardImageView = itemView.findViewById(R.id.onboardItemviewImageView);
        }
    }
}
