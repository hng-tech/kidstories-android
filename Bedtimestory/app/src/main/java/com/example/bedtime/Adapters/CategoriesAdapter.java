package com.example.bedtime.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedtime.Model.Category;
import com.example.bedtime.R;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Category> mCategories;
    String mType;

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
        }else {
            CategoryHolder hold = (CategoryHolder) holder;
            hold.mName.setText(category.getName());
        }

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

        }
    }
    class CategoryHolderRound extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mName;
        public CategoryHolderRound(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.cat_image);
            mName = itemView.findViewById(R.id.cat_name);

        }
    }
}
