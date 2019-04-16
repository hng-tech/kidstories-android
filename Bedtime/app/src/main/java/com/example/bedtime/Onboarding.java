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
import android.widget.Toast;

import com.example.bedtime.adapter.OnboardingAdapter;

public class Onboarding extends AppCompatActivity {

    private LinearLayout dotlayout;
    private ViewPager slideViewpager;
    private OnboardingAdapter onboardingAdapter;
    private TextView[] mDots;
    private Button mSkip;
    private Button mNext;
    private int mCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        dotlayout=findViewById(R.id.dotlayout);
        slideViewpager=findViewById(R.id.slide_pager);

        onboardingAdapter=new OnboardingAdapter(this);

        slideViewpager.setAdapter(onboardingAdapter);
        addDotsIndicator(0);
        slideViewpager.addOnPageChangeListener(viewListener);

        mSkip=findViewById(R.id.button);
        mNext=findViewById(R.id.button2);

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Onboarding.this,"Proceed to View Story ",Toast.LENGTH_LONG).show();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrent==1){
                   /* Intent intent=new Intent(Onboarding.this,MainActivity.class);
                    startActivity(intent);
                    Onboarding.this.finish();*/
                    Toast.makeText(Onboarding.this, "Can Now goto Main Activity of the Original App", Toast.LENGTH_LONG).show();
                }else{
                    slideViewpager.setCurrentItem(mCurrent +1);
                }
            }
        });

    }

        public  void addDotsIndicator(int position){
        mDots=new TextView[2];
        dotlayout.removeAllViews();
        for(int i=0;i< mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            dotlayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryFade));
        };
        }
        ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addDotsIndicator(i);
                mCurrent=i;
                if(i==0){
                    mNext.setEnabled(true);
                    mSkip.setEnabled(true);
                    mSkip.setVisibility(ViewPager.VISIBLE);
                    mNext.setText("Next");
                    mSkip.setText("Skip");
                }else if(i==mDots.length-1){
                    mNext.setEnabled(true);
                    mSkip.setEnabled(false);
                    mSkip.setVisibility(ViewPager.INVISIBLE);
                    mNext.setText("Done");




                }else{
                    mNext.setEnabled(true);
                    mSkip.setEnabled(true);
                    mSkip.setVisibility(ViewPager.VISIBLE);

                    mNext.setText("Next");
                    mSkip.setText("Skip");
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };
}
