package com.bignerdranch.andriod.tot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import database.NewsListDbSchema.NewsListBaseHelper;
import database.NewsListDbSchema.NewsListDbSchema;
import database.NewsListDbSchema.NewsListDbSchema.NewsListTable;

/**
 * Created by Roy on 2017/4/15.
 */

public class NewsFetchr {
    private static final String TAG = "NewsFetchr";

    private static List<String> mUrlList = new ArrayList<String>();

    private SQLiteDatabase mDatabase;

    private boolean exist = false;

    private static final String API_KEY = "d3ff2763088861953a3bd08427a1fdff";

    public byte[] getUrlByetes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        "with" + urlSpec);
            }

            int byteRead = 0;
            byte[] buffer = new byte[1024];
            while ((byteRead = in.read(buffer)) > 0){
                out.write(buffer,0,byteRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlByetes(urlSpec));
    }

    //http://v.juhe.cn/toutiao/index?type=top&key=d3ff2763088861953a3bd08427a1fdff
    public List<NewsItem> fetchItems(String type,String page,Context context){

        List<NewsItem> items = new ArrayList<>();

        try {
            //http://wangyi.butterfly.mopaasapp.com/news/api?type=war&page=1&limit=10
            String url = Uri.parse("http://wangyi.butterfly.mopaasapp.com/news/api")
                    .buildUpon()
                    .appendQueryParameter("type",type)
                    .appendQueryParameter("page",page)
                    .appendQueryParameter("limit","10")
                    .appendQueryParameter("format","json")
                    .appendQueryParameter("nojsoncallback","1")
                    .appendQueryParameter("extras","url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG ,"Received JSON: "+jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items,jsonBody,context);

        } catch (JSONException je) {
            Log.e(TAG ,"Failed to parse JSON"+je);
        }catch (IOException ioe){
            Log.e(TAG ,"Failed to fetch items"+ioe);
        }
        return items;
    }

    public void parseItems(List<NewsItem> items,JSONObject jsonBody,Context context) throws IOException,JSONException{

        JSONArray dataJsonArray = jsonBody.getJSONArray("list");

        for (int i = 0;i < dataJsonArray.length(); i++) {
            JSONObject newsJsonObeject = dataJsonArray.getJSONObject(i);

            NewsItem item = new NewsItem();
            String title = newsJsonObeject.getString("title");
            item.setTitle(title);

            String date = newsJsonObeject.getString("time");
            item.setDate(date);
            item.setTextUrl(newsJsonObeject.getString("docurl"));

            String url = newsJsonObeject.getString("imgurl");
            String fileName = getMD5(URLEncoder.encode(url, "utf-8"));
            String fileAdress = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ToT/MicroMsg/"+ fileName +".jpg";

            Bitmap bitmap = getbitmap(url);
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] bytes = baos.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }catch (Exception e1){
                Log.v("Erroros","压缩失败");
            }


            mDatabase = new NewsListBaseHelper(context).getWritableDatabase();


            Cursor cursor = mDatabase.rawQuery("SELECT * FROM NewsList WHERE image = ?", new String[]{fileAdress});
            while (cursor.moveToNext()) {
                title = cursor.getString(cursor.getColumnIndex("title"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                fileAdress = cursor.getString(cursor.getColumnIndex("image"));
                exist = true;

            }
            cursor.close();

            if (exist == true){
                bitmap = getDiskBitmap(fileAdress);
                Log.i("Exist","Exist:"+newsJsonObeject.getString("title"));
            }else {
                try {
                    saveJPGFile(bitmap,fileName);
                    Log.v("Downloadimage","保存图片成功");
                }catch (Exception e){
                    Log.v("getimage","保存图片失败");

                }
            }

            item.setPhotoBitmap(bitmap);
            item.setPhotoUrl(fileName);
            item.setType(newsJsonObeject.getString("channelname"));

            mDatabase.execSQL("INSERT INTO NewsList VALUES (NULL, ?, ?, ?)", new Object[]{title, date,fileAdress});
            mDatabase.close();

            exist = false;
            items.add(item);
        }
    }

    private static String getMD5(String url) {
        String result="";
        try {
            MessageDigest md=MessageDigest.getInstance("md5");
            md.update(url.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb=new StringBuilder();
            for(byte b:bytes){
                String str=Integer.toHexString(b&0xFF);
                if(str.length()==1){
                    sb.append("0");
                }
                sb.append(str);
            }
            result=sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    //保存图片到本地
    private void saveJPGFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ToT/MicroMsg/";
        File dirFile = new File(path);
        // 文件夹不存在则创建文件夹
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        Log.v(TAG, "创建文件夹成功");
        File myCaptureFile = new File(path + fileName + ".jpg");
        Log.v(TAG, "文件路径");

        try{
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            Log.v(TAG, "文件流");
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.v(TAG, "保存成功");
            bos.flush();
            bos.close();
//            if(bm.isRecycled()==false)
//            {
//                bm.recycle();
//                Log.v(TAG,"回收bitmap");
//            }
        }catch(Exception e){

        }
    }

    //获取bitmap
    public static Bitmap getbitmap(String imageUri) {
        Log.v(TAG, "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v(TAG, "image download finished." + imageUri);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "getbitmap bmp fail---");
            bitmap = null;
        }
        return bitmap;
    }

    //获取本地图片
    private Bitmap getDiskBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return bitmap;
    }

}
