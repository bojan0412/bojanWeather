package com.android.bojan.bojanweather.view.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.android.bojan.bojanweather.R;
import com.android.bojan.bojanweather.view.base.BojanWeatherApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by bojan
 * on 2018/8/23
 */
public class DBManager {

    public static final String DB_NAME="china_city.db";
    public static final String PACKAGE_NAME="com.android.bojan.bojanweather";
    public static final String DB_PATH="/data"+ Environment.getDataDirectory()
            .getAbsolutePath()+"/"+PACKAGE_NAME;

    private SQLiteDatabase database;
    private Context context;
    public DBManager(){

        openDatabase();
    }




    private void openDatabase()
    {
        this.database=this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    @Nullable
    private SQLiteDatabase openDatabase(String dbfile)
    {
        try{
            if(!(new File(dbfile).exists()))
            {
                InputStream is= BojanWeatherApplication.getContext().getResources().openRawResource(R.raw.china_city);
                FileOutputStream fos=new FileOutputStream(dbfile);
                int BUFFER_SIZE=40000;
                byte[] buffer=new byte[BUFFER_SIZE];
                int count;
                while((count=is.read(buffer))>0){
                    fos.write(buffer,0,count);
                }
                fos.close();
                is.close();

            }

            return SQLiteDatabase.openOrCreateDatabase(dbfile,null);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void closeDatabase() {
        if (this.database != null) {
            this.database.close();
        }
    }

    public SQLiteDatabase getDatabase(){return database;}

}
