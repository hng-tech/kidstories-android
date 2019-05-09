package com.dragonlegend.kidstories.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragonlegend.kidstories.Model.More;
import com.dragonlegend.kidstories.R;

import java.util.List;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MyViewHolder> {

    private List<More> moreList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

        }
    }


    public MoreAdapter(List<More> moreList) {
        this.moreList = moreList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        More more = moreList.get(position);
        holder.title.setText(more.getTitle());

    }

    @Override
    public int getItemCount() {
        return moreList.size();
    }
}
