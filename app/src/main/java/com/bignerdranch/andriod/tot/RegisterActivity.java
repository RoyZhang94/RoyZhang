package com.bignerdranch.andriod.tot;

import android.support.v4.app.Fragment;

/**
 * Created by Roy on 2017/4/12.
 */

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}
