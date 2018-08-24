package com.android.bojan.bojanweather.view.model.database;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class DetailORM {

    private int mIconRes;
    private String key;
    private String value;

    public DetailORM(int iconRes,String key,String value){
        this.key=key;
        this.value=value;
        mIconRes=iconRes;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public int getIconRes(){
        return mIconRes;
    }




}
