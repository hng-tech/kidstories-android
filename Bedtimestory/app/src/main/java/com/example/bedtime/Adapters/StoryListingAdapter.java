package com.example.bedtime.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bedtime.Model.Story;
import com.example.bedtime.R;

import java.util.List;

public class StoryListingAdapter  extends RecyclerView.Adapter<StoryListingAdapter.StoryHolder> {
    Context mContext;
    List<Story> mStories;

    public StoryListingAdapter(Context context, List<Story> stories) {
        mContext = context;
        mStories = stories;
    }

    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.post_single,viewGroup,false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryHolder storyHolder, int i) {
        Story story = mStories.get(i);
        storyHolder.mTitle.setText(story.getTitle());
        storyHolder.mTime.setText(story.getReleaseDate());
        Glide.with(mContext).load(story.getImage()).into(storyHolder.mImage);
    }

    @Override
    public int getItemCount() {
        return mStories !=null?mStories.size():0;
    }

    public void addStory(Story story){
        mStories.add(story);
        notifyItemInserted(mStories.size() !=0?mStories.size()-1:0);
    }
    public  void addStories(List<Story> stories){
        for(Story story:stories){
            addStory(story);
        }
    }
    public  void addStories(Story[] stories){
        for(Story story:stories){
            addStory(story);
        }
    }
    class StoryHolder extends RecyclerView.ViewHolder  {
        ImageButton mLike,mDislike;
        TextView mTitle,mTime;
        ImageView mImage,mAuthor_image;
        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.story_banner);
            mAuthor_image = itemView.findViewById(R.id.author_image);
            mTitle = itemView.findViewById(R.id.story_title);
            mTime = itemView.findViewById(R.id.story_publish_time);
            mLike = itemView.findViewById(R.id.like_button);
            mDislike = itemView.findViewById(R.id.dislike_button);
        }
    }
}
