package com.example.bedtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bedtime.Adapters.OnBoardingAdapter;

public class Onboarding extends AppCompatActivity {
    ViewPager mSlidePager;
    Button mSkipButton, mNextButton, mStartReadingButton;
    LinearLayout mSquareLayout, mBottomLayout;
    TextView[] mSquares;
    String[] titles;
    OnBoardingAdapter mOnBoardingAdapter;
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            if (i == mSquares.length - 1) {
                mBottomLayout.setVisibility(View.GONE);
                mStartReadingButton.setVisibility(View.VISIBLE);
            }
            else {
                addDotIndicator(i);
                mBottomLayout.setVisibility(View.VISIBLE);
                mStartReadingButton.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mSlidePager = findViewById(R.id.onboarding_vp);
        mSkipButton = findViewById(R.id.skip_on_boarding);
        mNextButton = findViewById(R.id.next_slide);
        mSquareLayout = findViewById(R.id.square_layout);
        mBottomLayout = findViewById(R.id.bottom_lay);
        mStartReadingButton = findViewById(R.id.start_reading);
        titles = getResources().getStringArray(R.array.onboarding_title);
        String[] bodies = getResources().getStringArray(R.array.onboarding_bodies);
        mOnBoardingAdapter = new OnBoardingAdapter(this, titles, bodies);
        mSlidePager.setAdapter(mOnBoardingAdapter);
        addDotIndicator(0);
        mSlidePager.addOnPageChangeListener(viewListener);

        //click listener for Next slide
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomLayout.setVisibility(View.GONE);
                mStartReadingButton.setVisibility(View.VISIBLE);
            }
        });
        //click listener for Skip Button to redirect users to Home activity
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });

        //click listener for Start reading Button to redirect users to Home activity
        mStartReadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
            }
        });


    }

    //intent method to parse onclick events
    private void startNextActivity() {
        Intent intent = new Intent(Onboarding.this, Home.class);
        startActivity(intent);
    }

    public void addDotIndicator(int position) {
        mSquares = new TextView[titles.length];
        mSquareLayout.removeAllViews();
        for (int i = 0; i < mSquares.length; i++) {
            mSquares[i] = new TextView(this);
            mSquares[i].setText(Html.fromHtml("&#8211;"));
            mSquares[i].setTextSize(40);
            mSquares[i].setTextColor(getResources().getColor(R.color.colorAsh));
            mSquareLayout.addView(mSquares[i]);

        }
        if (mSquares.length > 0) {
            mSquares[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
