package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dragonlegend.kidstories.AddStoryActivity;
import com.dragonlegend.kidstories.Model.Category;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.StoryListingActivity;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Category> mCategories;
    String mType;
    OnclickListener mListener = new OnclickListener() {
        @Override
        public void click( int pos) {
            startListing( pos);
        }
    };

     public interface OnclickListener {
        void click(int pos );
    }

    public CategoriesAdapter(Context context, List<Category> categories,String type) {
        mContext = context;
        mCategories = categories;
        mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


        if(mType.equals("home")){
             view = LayoutInflater.from(mContext).inflate(R.layout.category_home_single,
                    parent,false);
             return new CategoryHolderRound(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.category_single_card,
                    parent,false);
            return new CategoryHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Category category = mCategories.get(position);

        if(mType.equals("home")){
            CategoryHolderRound hold = (CategoryHolderRound) holder;
            hold.mName.setText(category.getName());
            if(category.getImage() != null){
                Glide.with(mContext).load(category.getImage())
                        .apply(RequestOptions.circleCropTransform())
                        .into(((CategoryHolderRound) holder).mImage);
            }
        }else {
            CategoryHolder hold = (CategoryHolder) holder;
            hold.mName.setText(category.getName());
            if(category.getImage() != null){
                Glide.with(mContext).load(category.getImage())
                        .into(hold.mImage);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    public void addCategory(Category category){
        mCategories.add(category);
        notifyItemInserted(mCategories.size() !=0?mCategories.size()-1:0);
    }
    public  void  addCategories(List<Category> categories){
        for(Category category:categories){
            addCategory(category);
        }
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mName;

        public CategoryHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.category_image);
            mName = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.click(getAdapterPosition());
                }
            });
        }
    }
    class CategoryHolderRound extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mName;
        public CategoryHolderRound(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.cat_image);
            mName = itemView.findViewById(R.id.cat_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.click(getAdapterPosition());
                }
            });

        }


    }
    private void startListing(int pos) {
         Intent intent = new Intent(mContext,StoryListingActivity.class);
        intent.putExtra(StoryListingActivity.CATEGORY_ID,mCategories.get(pos).getId());
        intent.putExtra(StoryListingActivity.CATEGORY_NAME,mCategories.get(pos).getName());

        mContext.startActivity(intent);
    }
    class AddNewHolder extends RecyclerView.ViewHolder {
        public AddNewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,AddStoryActivity.class);
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
