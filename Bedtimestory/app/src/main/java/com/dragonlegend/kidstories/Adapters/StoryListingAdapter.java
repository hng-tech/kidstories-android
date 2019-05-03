package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Story> mStories;

    ProgressBar holdProgress;

    public StoryListingAdapter(Context context, List<Story> stories) {
        mContext = context;
        mStories = stories;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (getActivityName().equals("Home")) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.post_single, viewGroup, false);
            return new StoryHolder(view);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.story_single, viewGroup, false);
            return new StoryGridHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Story story = mStories.get(i);
        if (getActivityName().equals("Home")) {
            StoryHolder storyHolder = (StoryHolder) holder;
            storyHolder.mTitle.setText(story.getTitle());
            storyHolder.mImgTitle.setText(story.getTitle());
            storyHolder.mTime.setText(story.getStoryDuration());
            storyHolder.dislikes.setText(String.valueOf(story.getDislikesCount()));
            storyHolder.likes.setText(String.valueOf(story.getLikesCount()));
            Glide.with(mContext).load(story.getImageUrl()).into(storyHolder.mImage);

            storyHolder.mLike.setOnClickListener(v -> {
                if (Prefs.getBoolean("isLoggedIn", false)) {
                    reactToStory(true, String.valueOf(story.getId()), i);
                    if (holdProgress != null) holdProgress.setVisibility(View.GONE);
                    holdProgress = ((StoryHolder) holder).reactionProgress;
                    holdProgress.setVisibility(View.VISIBLE);
//            int addLike = Integer.parseInt(storyHolder.likes.getText().toString()) + 1;
//            storyHolder.likes.setText(String.valueOf(addLike));
                } else
                    Toast.makeText(mContext, "You must be logged in to perform this operation", Toast.LENGTH_SHORT).show();


            });
            storyHolder.mDislike.setOnClickListener(v -> {
                if (Prefs.getBoolean("isLoggedIn", false)) {
                    reactToStory(false, String.valueOf(story.getId()), i);
                    if (holdProgress != null) holdProgress.setVisibility(View.GONE);
                    holdProgress = ((StoryHolder) holder).reactionProgress;
                    holdProgress.setVisibility(View.VISIBLE);
//            int addDislike = Integer.parseInt(storyHolder.dislikes.getText().toString()) + 1;
//            storyHolder.dislikes.setText(String.valueOf(addDislike));
                } else
                    Toast.makeText(mContext, "You must be logged in to perform this operation", Toast.LENGTH_SHORT).show();
            });
            if (story.getIsPremium() == 1) {
                storyHolder.mPremium.setVisibility(View.VISIBLE);
            }
        } else {
            StoryGridHolder storyGridHolder = (StoryGridHolder) holder;
            storyGridHolder.mTitle.setText(story.getTitle());
            if (story.getIsPremium() == 1) {
                storyGridHolder.mPremium.setVisibility(View.VISIBLE);
            }
            if (story.getImageUrl() != null) {
                Glide.with(mContext).load(story.getImageUrl()).into(storyGridHolder.mImage);

            }
        }
    }

    @Override
    public int getItemCount() {
        return mStories != null ? mStories.size() : 0;
    }

    public void addStory(Story story) {
        mStories.add(story);
        notifyItemInserted(mStories.size() != 0 ? mStories.size() - 1 : 0);
    }

    public void addStories(List<Story> stories) {
        for (Story story : stories) {
            addStory(story);
        }
    }

    public void addStories(Story[] stories) {
        for (Story story : stories) {
            addStory(story);
        }
    }

    public String getActivityName() {
        return mContext.getClass().getSimpleName();
    }

    class StoryGridHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mTitle, mPremium;

        public StoryGridHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.story_image);
            mTitle = itemView.findViewById(R.id.story_title);
            mPremium = itemView.findViewById(R.id.premium);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(), StoryDetail.class);
                    intent.putExtra(StoryDetail.STORY_ID, mStories.get(getAdapterPosition()).getId());
                    Prefs.putInt("story_id", mStories.get(getAdapterPosition()).getId());

                    Log.d("TAG", "onClick: -> " + Prefs.getInt("story_id", 0));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class StoryHolder extends RecyclerView.ViewHolder {
        ImageButton mLike, mDislike;
        TextView mTitle, mTime, mImgTitle, likes, dislikes, mPremium;
        ImageView mImage, mAuthor_image;
        ProgressBar reactionProgress;

        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.story_banner);
            mAuthor_image = itemView.findViewById(R.id.author_image);
            mTitle = itemView.findViewById(R.id.story_title);
            mImgTitle = itemView.findViewById(R.id.story_imgtitle);
            mTime = itemView.findViewById(R.id.story_publish_time);
            mLike = itemView.findViewById(R.id.like_button);
            mDislike = itemView.findViewById(R.id.dislike_button);
            likes = itemView.findViewById(R.id.likes);
            dislikes = itemView.findViewById(R.id.dislikes);
            reactionProgress = itemView.findViewById(R.id.reactionProgress);
            mPremium = itemView.findViewById(R.id.premium_text);

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(), StoryDetail.class);
                    intent.putExtra(StoryDetail.STORY_ID, mStories.get(getAdapterPosition()).getId());
                    Prefs.putInt("story_id", mStories.get(getAdapterPosition()).getId());

                    Log.d("TAG", "onClick: -> " + Prefs.getInt("story_id", 0));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void reactToStory(boolean isLike, String storyId, int pos) {

        String action = isLike ? "like" : "dislike";
        MainAplication.getApiInterface().reactToStory(action, storyId).enqueue(new Callback<StoryReactionResponse>() {
            @Override
            public void onResponse(Call<StoryReactionResponse> call, Response<StoryReactionResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoryReactionResponse reactionResponse = response.body();
                    holdProgress.setVisibility(View.GONE);
                    mStories.get(pos).setLikesCount(reactionResponse.getLikesCount());
                    mStories.get(pos).setDislikesCount(reactionResponse.getDislikesCount());
                    notifyDataSetChanged();
                    Toast.makeText(mContext, reactionResponse.getAction(), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StoryReactionResponse> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
