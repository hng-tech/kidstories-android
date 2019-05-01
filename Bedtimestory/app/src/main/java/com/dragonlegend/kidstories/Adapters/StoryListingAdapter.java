package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryReactionResponse;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.StoryDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        storyHolder.mImgTitle.setText(story.getTitle());
        storyHolder.mTime.setText(story.getStoryDuration());
        Glide.with(mContext).load(story.getImageUrl()).into(storyHolder.mImage);

        storyHolder.mLike.setOnClickListener(v -> reactToStory(true, story.getId()));
        storyHolder.mDislike.setOnClickListener(v -> reactToStory(false, story.getId()));
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
        TextView mTitle,mTime ,mImgTitle;
        ImageView mImage,mAuthor_image;
        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.story_banner);
            mAuthor_image = itemView.findViewById(R.id.author_image);
            mTitle = itemView.findViewById(R.id.story_title);
            mImgTitle = itemView.findViewById(R.id.story_imgtitle);
            mTime = itemView.findViewById(R.id.story_publish_time);
            mLike = itemView.findViewById(R.id.like_button);
            mDislike = itemView.findViewById(R.id.dislike_button);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(),StoryDetail.class);
                    intent.putExtra(StoryDetail.STORY_ID,mStories.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
    private void reactToStory(boolean isLike, int storyId){

        String action = isLike?  "like" :  "dislike";
        Client.getInstance().create(ApiInterface.class).reactToStory(action, String.valueOf(storyId)).enqueue(new Callback<BaseResponse<StoryReactionResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<StoryReactionResponse>> call, Response<BaseResponse<StoryReactionResponse>> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    StoryReactionResponse reactionResponse = response.body().getData();

                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                else Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<StoryReactionResponse>> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
