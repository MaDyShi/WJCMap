package com.wjc.wjcmap.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUtil {
    // 设置只能输入汉字
    public static void setInPutChinese(EditText et, int length){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                String speChat="[^\u4E00-\u9FA5]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(charSequence.toString());
                if(!matcher.find()){
                    return null;
                }else {
                    return "";
                }
            }
        };
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length),filter});
    }
}
