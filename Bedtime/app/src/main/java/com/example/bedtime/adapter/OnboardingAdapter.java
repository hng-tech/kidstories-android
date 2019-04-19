package com.example.bedtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardingAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public OnboardingAdapter(Context context){
        this.context=context;
    }
    // Array
    public int[] slide_images={
            R.drawable.walk1,
            R.drawable.walk2,


    };
    public String[]slide_heading={
            "Bedtime Stories For Kids","Add you own Stroies"
    };

    public String[] slide_desc={
      "Read free bedtime stories, fairy tales, poems for kids \n" +
              "and short stories for kids",
      "You can add your own bedtime stories, fairy tales, poems for kids and short kids"

    };



    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==(LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.onboarding_layout,container,false);

        ImageView slideImageView=(ImageView)view.findViewById(R.id.imageView2);
        TextView slideHeading=(TextView)view.findViewById(R.id.title);
        TextView slideDescription=(TextView)view.findViewById(R.id.slide_desc);
        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
