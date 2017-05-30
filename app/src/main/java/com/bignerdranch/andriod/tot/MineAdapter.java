package com.bignerdranch.andriod.tot;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roy on 2017/5/4.
 */

public class MineAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public MineAdapter(Context context,List<HashMap<String, Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */
    public final class ListItem{
        public TextView textview_title_mine;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = null;
        if(convertView == null){
            item = new ListItem();
            //获得组件，实例化组件
            convertView=layoutInflater.inflate(R.layout.fragment_textview_mine, null);
            item.textview_title_mine = (TextView)convertView.findViewById(R.id.textview_title_mine);
            convertView.setTag(item);
        }else{
            item=(ListItem) convertView.getTag();
        }
        //绑定数据
        item.textview_title_mine.setText((String)data.get(position).get("textview_title_mine"));
        return convertView;
    }
}
