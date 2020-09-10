package com.officialshopwala.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {

    ArrayList<OnboardItem> onboardItems = new ArrayList<>();

    private ViewPagerAdapter viewPagerAdapter;

    ViewPager2 onBoardViewPager;

    LinearLayout footerLinearLayout;

    Button signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        onBoardViewPager = findViewById(R.id.onBoardViewPager);
        footerLinearLayout = findViewById(R.id.footerLinearLayout);
        signUpTextView = findViewById(R.id.signupTextView);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBoardViewPager.getCurrentItem()+1 < viewPagerAdapter.getItemCount()) {
                    onBoardViewPager.setCurrentItem(onBoardViewPager.getCurrentItem()+1);
                } else {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                    finish();
                }
            }
        });

        setUpOnboardItems();
        setViewPageAdapter();
        setOnboardingIndicators();
        setCurrentIndicator(0);

        onBoardViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }

    private void setUpOnboardItems() {

        OnboardItem onboardItem_1 = new OnboardItem();
        onboardItem_1.setOnboardImage(R.drawable.onboarding_one);
        onboardItem_1.setTitle(getString(R.string.onBoardOneTitle));
        onboardItem_1.setDescription(getString(R.string.onBoardOneDescription));
        onboardItems.add(onboardItem_1);

        OnboardItem onboardItem_2 = new OnboardItem();
        onboardItem_2.setOnboardImage(R.drawable.onboarding_two);
        onboardItem_2.setTitle(getString(R.string.onBoardTwoTitle));
        onboardItem_2.setDescription(getString(R.string.onBoardTwoDescription));
        onboardItems.add(onboardItem_2);

        OnboardItem onboardItem_3 = new OnboardItem();
        onboardItem_3.setOnboardImage(R.drawable.onboarding_three);
        onboardItem_3.setTitle(getString(R.string.onBoardThreeTitle));
        onboardItem_3.setDescription(getString(R.string.onBoardThreeDescription));
        onboardItems.add(onboardItem_3);

    }

    private void setViewPageAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), onboardItems);
        onBoardViewPager.setAdapter(viewPagerAdapter);
    }

    private void setOnboardingIndicators() {
        ImageView[] indicators = new ImageView[viewPagerAdapter.getItemCount()];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(4, 0, 4, 10);

        for ( int i = 0 ; i < indicators.length; i++ ) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboard_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            footerLinearLayout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator ( int index ) {
        int childCount = footerLinearLayout.getChildCount();
        for ( int i = 0 ; i < childCount; i++ ) {
            ImageView imageView = (ImageView)footerLinearLayout.getChildAt(i);
            if ( i == index ) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.onboard_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),
                        R.drawable.onboard_indicator_inactive
                ));
            }
        }
    }

}