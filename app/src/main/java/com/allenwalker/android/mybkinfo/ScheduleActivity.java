package com.allenwalker.android.mybkinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ScheduleActivity extends SingleFragmentActivity {

    private static final String EXTRA_MSSV =
            "com.allenwalker.android.mybkinfo.mssv";

    public static Intent newIntent(Context packageContext, String mssv) {
        Intent i = new Intent(packageContext, ScheduleActivity.class);
        i.putExtra(EXTRA_MSSV, mssv);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        String mssv = getIntent().getStringExtra(EXTRA_MSSV);
        return ScheduleFragment.newInstance(mssv);
    }
}
