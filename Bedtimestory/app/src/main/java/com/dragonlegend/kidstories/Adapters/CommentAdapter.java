package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Api.Responses.CommentResponse;
import com.dragonlegend.kidstories.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.ViewHolder>{
private static final String TAG="CommentAdapter";
    private ArrayList<CommentResponse> commentsList;
  private Context mcontext;

public CommentAdapter(Context context,ArrayList<CommentResponse>commentsList){
 this.commentsList = commentsList;
 mcontext=context;
}

public  void setComment(ArrayList<CommentResponse> commentsList){
    this.commentsList = commentsList;
    notifyDataSetChanged();
}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.coment_item,parent,false);
    ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder:called.");
        CommentResponse commentResponse = commentsList.get(position);
        Glide.with(mcontext).asBitmap().load(commentResponse.getUserImage()).into(holder.imageView);
        holder.username.setText(commentResponse.getPublisher());
        holder.comment.setText(commentResponse.getComment());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
public TextView username,comment;
RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.user_image);
            username=itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.Comment);
            parentLayout=itemView.findViewById(R.id.parnt_layout);
        }
    }
}
