package com.dragonlegend.kidstories.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragonlegend.kidstories.Model.More;
import com.dragonlegend.kidstories.MoreActivity;
import com.dragonlegend.kidstories.ProfileActivity;
import com.dragonlegend.kidstories.R;
import com.pixplicity.easyprefs.library.Prefs;

public class BottomMenuFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    public BottomMenuFragment() {
        // Required empty public constructor
    }
    LinearLayout b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_menu_dialog, container, false);

         b =  v.findViewById(R.id.donate_url);
        b.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cat_activity:
                //start category activity .

                break;
            case R.id.profile_activity:
                //start Profile activity .

                break;
                case R.id.moreactivity:
                //start category activity .
                    Intent intent = new Intent(getActivity(), MoreActivity.class);
                    startActivity(intent);
                    break;
            case R.id.add_story_activity:
                //start addstory activity .

                break;
            case R.id.donate_url:
                //do ur code;
                String url = "https://paystack.com/pay/kidstoriesapp";
                Intent d = new Intent(Intent.ACTION_VIEW);
                d.setData(Uri.parse(url));
                startActivity(d);
                break;
            case R.id.signout:
                //do ur code;

            default:
                //do ur code;
        }
    }
}