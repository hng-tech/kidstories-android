package com.dragonlegend.kidstories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlegend.kidstories.Adapters.MoreAdapter;
import com.dragonlegend.kidstories.Model.More;
import com.dragonlegend.kidstories.Utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {

    private List<More> moreList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoreAdapter mAdapter;
    TextView upgrade,membership_stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content__more_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        upgrade = (TextView)findViewById(R.id.upgrade);
        membership_stat =(TextView)findViewById(R.id.membership_stat);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoreAdapter(moreList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep more_list_rowxml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep more_list_rowxml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);




// row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                More more = moreList.get(position);
                Toast.makeText(getApplicationContext(), more.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    /**
     * Prepares sample data to provide data set to adapter
     */
    private void prepareMovieData() {
        More more = new More("Donate");
        moreList.add(more);

        more = new More("Profile");
        moreList.add(more);

        more = new More("Privacy Policy");
        moreList.add(more);

        more = new More("Term of Use");
        moreList.add(more);

        more = new More("Follow us on Twitter");
        moreList.add(more);
//        more = new More("Follow us on Facebook");
//        moreList.add(more);

        more = new More("Follow us on Instagaram");
        moreList.add(more);


        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }

}

