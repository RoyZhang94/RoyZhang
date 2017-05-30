package com.bignerdranch.andriod.tot;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import database.NewsListDbSchema.NewsListDbSchema;
import database.NewsListDbSchema.NewsListDbSchema.NewsListTable;

import static android.R.attr.path;

/**
 * Created by Roy on 2017/4/15.
 */

public class NewsListFragment extends Fragment{
    private static final String TAG = "NewsListFragment";

    private RecyclerView mNewsRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NewsItem> mItems = new ArrayList<>();
    private List<NewsItem> mItemsAdd = new ArrayList<>();
    private ThumbnailDownloader<NewsHolder> mThumbnailDownloader;
    private String mType = "shehui";
    private String mPage = "1";
    private int mCount = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsAdapter mAdapter;
    private static final int REFRESH_COMPLETE = 0X110;
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;
    private Context mContext;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    new FetchItemsTask().execute();
                    mSwipeRefreshLayout.setRefreshing(false);
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    public static NewsListFragment newInstance(){
        return new NewsListFragment();
    }

    public void setType(String type) {
        this.mType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getContext();
        new FetchItemsTask().execute();

        if (getAPNType(mContext) == -1){
            Toast.makeText(getActivity(), "当前无网络不可用，请检查你的设置", Toast.LENGTH_SHORT).show();
        }

//        Handler responseHandler = new Handler();
//        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
//        mThumbnailDownloader.setThumbnailDownloadListener(
//               new ThumbnailDownloader.ThumbnailDownloadListener<NewsHolder>(){
//                   @Override
//                   public void onThumbnailDownloaded(NewsHolder newsHolder,Bitmap bitmap){
//                       Drawable drawable = new BitmapDrawable(getResources(),bitmap);
//                       newsHolder.bindDrawable(drawable);
//                   }
//               }
//        );
//        mThumbnailDownloader.start();
//        mThumbnailDownloader.getLooper();
//        Log.i(TAG,"Background thread started");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_news_list,container,false);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mNewsRecyclerView = (RecyclerView)v.findViewById(R.id.news_recycler_view);
        mNewsRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.news_swiperefreshlayout);
        mSwipeRefreshLayout.setSize(mSwipeRefreshLayout.DEFAULT);
        mSwipeRefreshLayout.setProgressViewEndTarget(true,100);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE,500);
            }
        });



        if (isAdded()){
            mAdapter = new NewsAdapter(mItemsAdd);
            mNewsRecyclerView.setAdapter(mAdapter);
        }

        mNewsRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        //自定义分割线高度和颜色
        //mNewsRecyclerView.addItemDecoration(new RecyclerDecoration(getActivity(),LinearLayoutManager.VERTICAL,10,getResources().getColor(R.color.light_blue)));
        //updateUI();
        mNewsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
//        mThumbnailDownloader.clearQueue();
    }
    @Override
    public void onDestroy(){
            super.onDestroy();
            mThumbnailDownloader.quit();
            Log.i(TAG,"Background thread destroyed");
        }

//    private void setupAdapter(){
//        if (isAdded()){
//            mNewsRecyclerView.setAdapter(new NewsAdapter(mItems));
//        }
//    }


    private class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitltTextView;
        private TextView mDateTextView;
        private ImageView mNewsPhotoImageView;
        private ProgressBar mLoadProgressBar;
        private TextView mLoadTextView;
        private NewsItem mItem;


        public NewsHolder(View itemView ,int viewType){
            super(itemView);
            if (viewType == NORMAL_TYPE){
                itemView.setOnClickListener(this);
                mTitltTextView = (TextView)itemView.findViewById(R.id.news_title);
                mDateTextView = (TextView)itemView.findViewById(R.id.news_date);
                mNewsPhotoImageView = (ImageView)itemView.findViewById(R.id.news_photo);
            }else if (viewType == FOOT_TYPE){
                mLoadProgressBar = (ProgressBar)itemView.findViewById(R.id.load_progressbar);
                mLoadTextView = (TextView)itemView.findViewById(R.id.load_textview);
            }

        }

        public void bindNewsItem(NewsItem item){
            mItem = item;

           // SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

            mTitltTextView.setText(mItem.toString());
            mDateTextView.setText(mItem.getDate());
            Log.i(TAG,""+mItem.getPhotoBitmap());
            Drawable drawable = new BitmapDrawable(getResources(),mItem.getPhotoBitmap());
            mNewsPhotoImageView.setImageDrawable(drawable);

        }

        public void bindDrawable(Drawable drawable){
            mNewsPhotoImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View view){
            Intent intent = NewsActivity.newIntent(getActivity(),mItem.getTextUrl());
            startActivity(intent);
        }
    }


    private class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
        private List<NewsItem> mNewsItems;

        public NewsAdapter(List<NewsItem> newsItems) {
            mNewsItems = newsItems;
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_news,parent,false);
            return new NewsHolder(view,viewType);
        }

        @Override
        public void onBindViewHolder (NewsHolder newsHolder,int position){
            NewsItem newsItem = mNewsItems.get(position);
            newsHolder.bindNewsItem(newsItem);

//            Drawable placeholder = ContextCompat.getDrawable(getActivity(), R.drawable.load);
//            newsHolder.bindDrawable(placeholder);
//            mThumbnailDownloader.queueThumbnail(newsHolder, newsItem.getPhotoUrl());

        }

        @Override
        public int getItemCount() {
            return mNewsItems.size();
        }

    }


    private class FetchItemsTask extends AsyncTask<Void,Void,List<NewsItem>>{
        @Override
        protected List<NewsItem> doInBackground(Void... params){
            return new NewsFetchr().fetchItems(mType,mPage,mContext);
        }

        @Override
        protected void onPostExecute(List<NewsItem> items){
            mItems = items;
            mItemsAdd.addAll(items);
            mAdapter.notifyDataSetChanged();

        }
    }

    private void loadMoreData() {
        mCount++;
        mPage = String.valueOf(mCount);
        new FetchItemsTask().execute();
    }


    //判断RecyclerView是否达到底部
    public static boolean isVisBottom(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){
            return true;
        }else {
            return false;
        }
    }

    //获取当前网络连接类型
    public static int getAPNType(Context context){
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null){
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            netType = 2;
        }

        else if(nType==ConnectivityManager.TYPE_WIFI){
            netType = 1;
        }
        return netType;
    }
}
