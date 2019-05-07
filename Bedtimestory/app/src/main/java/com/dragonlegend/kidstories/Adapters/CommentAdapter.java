package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Api.Responses.CommentResponse;
import com.dragonlegend.kidstories.Model.Comment;
import com.dragonlegend.kidstories.R;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    private List<Comment> commentsList;
    private Context mcontext;


    public CommentAdapter(Context context, List<Comment> commentsList) {
        this.commentsList = commentsList;
        mcontext = context;
    }

    public void setComment(List<Comment> commentsList) {
        this.commentsList = commentsList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:called.");
        Comment comm = commentsList.get(position);
        if (comm.getUser().getImage() != null) {
            Glide.with(mcontext).asBitmap().load(comm.getUser().getImage()).into(holder.imageView);
        }
        if (position % 2 == 0) {
            holder.parentLayout.setBackgroundColor(mcontext.getResources().getColor(R.color.colorAshLight));
        }
        holder.username.setText(comm.getUser().getFullName());
        holder.comment.setText(comm.getBody());



    }



    @Override
    public int getItemCount() {
        return commentsList !=null?commentsList.size():0;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,delete;
        public TextView username, comment;
        ConstraintLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.commenter_image);
            username = itemView.findViewById(R.id.commenter);
            comment = itemView.findViewById(R.id.comment);
            parentLayout = itemView.findViewById(R.id.comment_cl);
            delete=itemView.findViewById(R.id.comment_delete);


//            mdelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }
}
