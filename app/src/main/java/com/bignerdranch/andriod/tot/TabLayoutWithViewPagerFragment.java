package com.bignerdranch.andriod.tot;

/**
 * Created by Roy on 2017/4/22.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by mChenys on 2016/5/28.
 */
public class TabLayoutWithViewPagerFragment extends Fragment {

    String[] mTitle = new String[9];
    TabLayout mTabLayout;
    ViewPager mViewPager;
    private View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (v == null){
            v = inflater.inflate(R.layout.fragment_tablayout_with_viewpager,container,false);

            initData();
            mTabLayout = (TabLayout)v.findViewById(R.id.tablayout_fragment);
            mViewPager = (ViewPager)v.findViewById(R.id.viewpager_fragment);
            mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
                //此方法用来显示tab上的名字
                @Override
                public CharSequence getPageTitle(int position) {
                    return mTitle[position % mTitle.length];
                }

                @Override
                public Fragment getItem(int position) {
                    //创建Fragment并返回
                    NewsListFragment fragment = new NewsListFragment();
                    //TabFragment fragment = new TabFragment();
//                    switch (position){
//                        case
//                    }
                    //fragment.setType(mTitle[position % mTitle.length]);
                    switch (position){
                        case 0:
                            fragment.setType("war");
                            break;
                        case 1:
                            fragment.setType("sport");
                            break;
                        case 2:
                            fragment.setType("tech");
                            break;
                        case 3:
                            fragment.setType("edu");
                            break;
                        case 4:
                            fragment.setType("ent");
                            break;
                        case 5:
                            fragment.setType("money");
                            break;
                        case 6:
                            fragment.setType("gupiao");
                            break;
                        case 7:
                            fragment.setType("travel");
                            break;
                        case 8:
                            fragment.setType("lady");
                            break;
                    }
                    return fragment;
                }

                @Override
                public int getCount() {
                    return mTitle.length;
                }
            });
            //将ViewPager关联到TabLayout上
            mTabLayout.setupWithViewPager(mViewPager);

//        设置监听,注意:如果设置了setOnTabSelectedListener,则setupWithViewPager不会生效
//        因为setupWithViewPager内部也是通过设置该监听来触发ViewPager的切换的.
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        那我们如果真的需要监听tab的点击或者ViewPager的切换,则需要手动配置ViewPager的切换,例如:
            mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    //切换ViewPager
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        return v;
    }

    private void initData() {
        for (int i = 0; i < 9; i++) {
            switch (i){
                case 0:
                    mTitle[i] = "军事";
                    break;
                case 1:
                    mTitle[i] = "体育";
                    break;
                case 2:
                    mTitle[i] = "科技";
                    break;
                case 3:
                    mTitle[i] = "教育";
                    break;
                case 4:
                    mTitle[i] = "娱乐";
                    break;
                case 5:
                    mTitle[i] = "财经";
                    break;
                case 6:
                    mTitle[i] = "股票";
                    break;
                case 7:
                    mTitle[i] = "旅游";
                    break;
                case 8:
                    mTitle[i] = "女人";
                    break;
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) v.getParent()).removeView(v);
    }
}