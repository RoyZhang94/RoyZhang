package com.bignerdranch.andriod.tot;

import android.support.v4.app.Fragment;

/**
 * Created by Roy on 2017/4/15.
 */

public class NewsListAcitivity extends  SingleFragmentActivity {
    @Override
    public Fragment createFragment(){
        return NewsListFragment.newInstance();
    }
}
