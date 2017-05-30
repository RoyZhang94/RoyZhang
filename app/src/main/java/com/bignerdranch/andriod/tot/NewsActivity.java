package com.bignerdranch.andriod.tot;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Roy on 2017/4/15.
 */

public class NewsActivity extends SingleFragmentActivity {
    private static final String EXTRA_NEWS_URL = "com.bignerdranch.android.tot.news_url";

    public static Intent newIntent(Context packageContext,String url){
        Intent intent = new Intent(packageContext,NewsActivity.class);
        intent.putExtra(EXTRA_NEWS_URL,url);
        return intent;
    }
    @Override
    public Fragment createFragment(){
        String newsUrl = (String)getIntent()
                .getSerializableExtra(EXTRA_NEWS_URL);
        return NewsFragment.newInstance(newsUrl);
    }
}
