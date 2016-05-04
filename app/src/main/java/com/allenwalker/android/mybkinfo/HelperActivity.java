package com.allenwalker.android.mybkinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class HelperActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, HelperActivity.class);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return new HelperFragment();
    }
}
