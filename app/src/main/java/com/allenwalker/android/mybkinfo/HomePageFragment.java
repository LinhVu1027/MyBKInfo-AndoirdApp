package com.allenwalker.android.mybkinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class HomePageFragment extends Fragment {

    private Button mScheludeButton;
    private Button mTestScheludeButton;
    private Button mTestPointButton;
    private ImageButton mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);

        mScheludeButton = (Button)v.findViewById(R.id.home_page_schedule);
        mScheludeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeFunction = "schedule";
                Intent i = RequestActivity.newIntent(getActivity(), typeFunction);
                startActivity(i);
            }
        });

        mTestScheludeButton = (Button)v.findViewById(R.id.home_page_test_schedule);
        mTestScheludeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeFunction = "testschedule";
                Intent i = RequestActivity.newIntent(getActivity(), typeFunction);
                startActivity(i);
            }
        });

        mTestPointButton = (Button)v.findViewById(R.id.home_page_test_point);
        mTestPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeFunction = "testpoint";
                Intent i = RequestActivity.newIntent(getActivity(), typeFunction);
                startActivity(i);
            }
        });

        mHelper = (ImageButton)v.findViewById(R.id.home_page_helper);
        mHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = HelperActivity.newIntent(getActivity());
                startActivity(i);
            }
        });

        return v;
    }
}
