package com.android.bojan.bojanweather.view.model.database;

/**
 * Create by bojan
 * on 2018/8/24
 */
public class LifeIndexORM {


    private String con_brif;
    private String con_txt;
    private int iconRes;

    public LifeIndexORM(String bfir,String txt,int icon){
        con_brif=bfir;
        con_txt=txt;
        iconRes=icon;
    }

    public String getCon_brif(){
        return con_brif;
    }

    public String getCon_txt(){
        return con_txt;
    }

    public int getIconRes(){
        return iconRes;
    }
}
