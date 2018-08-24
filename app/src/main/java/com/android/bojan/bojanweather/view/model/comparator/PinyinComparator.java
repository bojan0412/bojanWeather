package com.android.bojan.bojanweather.view.model.comparator;

import com.android.bojan.bojanweather.view.model.data.City;
import com.android.bojan.bojanweather.view.model.data.Province;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Comparator;

/**
 * Create by bojan
 * on 2018/8/23
 */
public class PinyinComparator implements Comparator<Object> {
    @Override
    public int compare(Object s1,Object s2){

        if(s1 instanceof City && s2 instanceof City){

            String pinyin1= Pinyin.toPinyin(((City) s1).CityName,"").substring(0,1).toUpperCase();
            String pinyin2=Pinyin.toPinyin(((City) s2).CityName,"").substring(0,1).toUpperCase();
            return pinyin1.compareTo(pinyin2);

        } else
        if (s1 instanceof Province && s2 instanceof Province){
            String pinyin1=Pinyin.toPinyin(((Province) s1).ProName,"").substring(0,1).toUpperCase();
            String pinyin2=Pinyin.toPinyin(((Province) s2).ProName,"").substring(0,1).toUpperCase();

            return pinyin1.compareTo(pinyin2);
        }

        return 0;
    }
}
