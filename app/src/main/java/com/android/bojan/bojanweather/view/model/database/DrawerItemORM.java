package com.android.bojan.bojanweather.view.model.database;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Create by bojan
 * on 2018/8/16
 */
@Table("drawer_item")
public class DrawerItemORM {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String mCity;
    private String mTemp;
    private String mCode;

    public DrawerItemORM(String city, String temp, String code) {
        mCity = city;
        mTemp = temp;
        mCode = code;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String temp) {
        mTemp = temp;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
