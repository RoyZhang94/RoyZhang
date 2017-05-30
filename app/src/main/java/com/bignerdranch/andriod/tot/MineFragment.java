package com.bignerdranch.andriod.tot;

/**
 * Created by Roy on 2017/4/14.
 */

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author syz
 * @date 2016-4-14
 */
public class MineFragment extends Fragment {
    private ListView mListView = null;
    private ShapeImageView mLoginShapeImageView;
    private ImageButton mSettingImageButton;
    private Button mNightModeButton;
    private Button mTextModeButton;
    private Button mOfflineReadButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_listview,container,false);
        mListView=(ListView)v.findViewById(R.id.mine_listview);

        View headView = View.inflate(getContext(),R.layout.fragment_login_setting_mine,null);
        mLoginShapeImageView = (ShapeImageView)headView.findViewById(R.id.fragment_mine_login);
        mLoginShapeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "登录", Toast.LENGTH_SHORT).show();
            }
        });
        mSettingImageButton = (ImageButton)headView.findViewById(R.id.setting_button);
        mSettingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
            }
        });
        mNightModeButton = (Button)headView.findViewById(R.id.night_mode);
        mNightModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "夜间模式", Toast.LENGTH_SHORT).show();
            }
        });
        mTextModeButton = (Button)headView.findViewById(R.id.text_mode);
        mTextModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "文字模式", Toast.LENGTH_SHORT).show();
            }
        });
        mOfflineReadButton = (Button)headView.findViewById(R.id.offline_read);
        mOfflineReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "离线阅读", Toast.LENGTH_SHORT).show();
            }
        });

        mListView.addHeaderView(headView);

        List<HashMap<String, Object>> list = getData();
        mListView.setAdapter(new MineAdapter(getActivity(),list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position 如果有headview  position =0 的第一个为headview
                Toast.makeText(getActivity()," position:" + position, Toast.LENGTH_SHORT).show();

                //id：如果有headview或者footview  则这两个view的id为-1
                if (id == -1) {
                    Toast.makeText(getContext()," head:" + id, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "id:" + id, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public List<HashMap<String, Object>> getData(){
        List<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 7; i++) {
            HashMap<String, Object> map=new HashMap<String, Object>();
            switch (i){
                case 0:
                    map.put("textview_title_mine", "我的消息");
                    break;
                case 1:
                    map.put("textview_title_mine", "我的关注");
                    break;
                case 2:
                    map.put("textview_title_mine", "我的收藏");
                    break;
                case 3:
                    map.put("textview_title_mine", "我的评论");
                    break;
                case 4:
                    map.put("textview_title_mine", "意见反馈");
                    break;
                case 5:
                    map.put("textview_title_mine", "红包");
                    break;
                case 6:
                    map.put("textview_title_mine", "广告");
                    break;
            }
            list.add(map);
        }
        return list;
    }
}