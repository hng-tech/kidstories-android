package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.dragonlegend.kidstories.Api.Responses.BookmarkResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryReactionResponse;
import com.dragonlegend.kidstories.Bookmark;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.StoryDetail;
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Story> mStories;
    int i;

    ProgressBar holdProgress;

    public BookListingAdapter(Context context, List<Story> stories) {
        mContext = context;
        mStories = stories;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.post_single, viewGroup, false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Story story = mStories.get(i);
        if (getActivityName().equals("Home") || getActivityName().equals("Bookmark")) {
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

            storyHolder.setPremiumStatus(story.getIsPremium());
        }
        else {
            StoryGridHolder storyGridHolder = (StoryGridHolder) holder;
            storyGridHolder.mTitle.setText(story.getTitle());
            if (story.getIsPremium() == 1) {
                storyGridHolder.mPremium.setVisibility(View.VISIBLE);
            }
            if (story.getImageUrl() != null) {
                Glide.with(mContext).load(story.getImageUrl()).into(storyGridHolder.mImage);

            }
            storyGridHolder.setPremiumStatus(story.getIsPremium());

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
    public void removeAllStories(){
        mStories = new ArrayList<>();
        notifyDataSetChanged();
    }
    public String getActivityName() {
        return mContext.getClass().getSimpleName();
    }

    class StoryGridHolder extends RecyclerView.ViewHolder {
        ImageView mImage, delete;
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
        void setPremiumStatus(int v){
            if(v > 0){
                mPremium.setVisibility(View.VISIBLE);
            }else {
                mPremium.setVisibility(View.GONE);
            }
        }

    }

    class StoryHolder extends RecyclerView.ViewHolder {
        ImageButton mLike, mDislike;
        TextView mTitle, mTime, mImgTitle, likes, dislikes, mPremium;
        ImageView mImage, mAuthor_image, delete;
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
            delete = itemView.findViewById(R.id.delete);
            reactionProgress = itemView.findViewById(R.id.reactionProgress);
            mPremium = itemView.findViewById(R.id.premium_text);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent = new Intent(mContext.getApplicationContext(), StoryDetail.class);
                        intent.putExtra(StoryDetail.STORY_ID, mStories.get(getAdapterPosition()).getId());
                        intent.putExtra("type", "fav");
                        Prefs.putInt("story_id", mStories.get(getAdapterPosition()).getId());

                        Log.d("TAG", "onClick: -> " + Prefs.getInt("story_id", 0));
                        mContext.startActivity(intent);
                }
            });
            if (getActivityName().equals(Bookmark.class.getSimpleName())){
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are you sure you want to delete this story from favourite?")
                                .setPositiveButton("I am sure", (dialog, which) -> deleteBookmark(mStories.get(getAdapterPosition()).getId())).setNegativeButton("Cancel", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }


        }
         void setPremiumStatus(int v){
            if(v > 0){
                mPremium.setVisibility(View.VISIBLE);
            }else {
                mPremium.setVisibility(View.GONE);
            }
        }
    }

    private void reactToStory(boolean isLike, String storyId, int pos) {

        String action = isLike ? "like" : "dislike";
        MainAplication.getApiInterface().reactToStory(action, storyId).enqueue(new Callback<StoryReactionResponse>() {
            @Override
            public void onResponse(Call<StoryReactionResponse> call, Response<StoryReactionResponse> response) {
                holdProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoryReactionResponse reactionResponse = response.body();
                    mStories.get(pos).setLikesCount(reactionResponse.getLikesCount());
                    mStories.get(pos).setDislikesCount(reactionResponse.getDislikesCount());
                    notifyDataSetChanged();
                    Toast.makeText(mContext, reactionResponse.getAction(), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(mContext, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StoryReactionResponse> call, Throwable t) {
                holdProgress.setVisibility(View.GONE);
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteBookmark(int story_id) {
        MainAplication.getApiInterface().deleteBookmark(story_id).enqueue(new Callback<BookmarkResponse>() {
            @Override
            public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BookmarkResponse> call, Throwable t) {

            }
        });
    }
}
