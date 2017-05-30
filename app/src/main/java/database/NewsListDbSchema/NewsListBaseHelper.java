package database.NewsListDbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.NewsListDbSchema.NewsListDbSchema.NewsListTable;

/**
 * Created by Roy on 2017/4/12.
 */

public class NewsListBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATEBASE_NAME = "newslistBase.db";

    public NewsListBaseHelper(Context context){
        super(context,DATEBASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+ NewsListTable.NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsListTable.Cols.TITLE +","+
                NewsListTable.Cols.DATE +","+
                NewsListTable.Cols.IMAGE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
