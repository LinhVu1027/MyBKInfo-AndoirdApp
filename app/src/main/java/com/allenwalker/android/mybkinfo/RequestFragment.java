package com.allenwalker.android.mybkinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RequestFragment extends Fragment {

    private static final String ARG_TYPE_FUNCTION = "arg_type_function";

    private EditText mRequestEditText;
    private Button mAcceptButton;
    private String mMSSV;
    private String mTypeFunction;

    public static RequestFragment newInstance(String typeFunction) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE_FUNCTION, typeFunction);

        RequestFragment fragment = new RequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypeFunction = getArguments().getString(ARG_TYPE_FUNCTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request, container, false);

        mRequestEditText = (EditText)v.findViewById(R.id.request_MSSV);
        mRequestEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mMSSV = s.toString();
            }
        });
        mRequestEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    mAcceptButton.performClick();
                    return true;
                }
                return false;
            }
        });

        mAcceptButton = (Button)v.findViewById(R.id.request_accept);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTypeFunction.equals("schedule")) {
                    Intent i = ScheduleActivity.newIntent(getActivity(), mMSSV);
                    startActivity(i);
                } else if (mTypeFunction.equals("testschedule")) {
                    Intent i = TestScheduleActivity.newIntent(getActivity(), mMSSV);
                    startActivity(i);
                } else if (mTypeFunction.equals("testpoint")) {
                    Intent i = TestPointActivity.newIntent(getActivity(), mMSSV);
                    startActivity(i);
                }
            }
        });

        return v;
    }
}
