package com.bignerdranch.andriod.tot;

/**
 * Created by Roy on 2017/4/14.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author syz
 * @date 2016-4-14
 */
public class HomeFragment extends Fragment {

    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_tablayout_with_viewpager, container, false);
        }


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
    }
}