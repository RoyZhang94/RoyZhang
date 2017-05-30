package com.bignerdranch.andriod.tot;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ToTActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ToTFragment();
    }

}
