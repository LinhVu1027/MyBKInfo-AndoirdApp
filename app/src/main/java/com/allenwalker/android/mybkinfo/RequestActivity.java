package com.allenwalker.android.mybkinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class RequestActivity extends SingleFragmentActivity {

    private static final String EXTRA_TYPE_FUNCTION =
            "com.allenwalker.android.mybkinfo.type_function";

    public static Intent newIntent(Context packageContext, String typeFunction) {
        Intent i = new Intent(packageContext, RequestActivity.class);
        i.putExtra(EXTRA_TYPE_FUNCTION, typeFunction);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        String typeFunction = getIntent().getStringExtra(EXTRA_TYPE_FUNCTION);
        return RequestFragment.newInstance(typeFunction);
    }
}
