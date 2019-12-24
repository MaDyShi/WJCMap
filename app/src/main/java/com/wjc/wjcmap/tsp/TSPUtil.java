package com.wjc.wjcmap.tsp;


import java.util.ArrayList;
import java.util.List;

public class TSPUtil {

    public static void main(String[] args) {
        String s= "abcd";
        char[] shu = s.toCharArray();
        //从第0个位置开始
        List<String> list = sortPoint(shu, 0);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        }

    }
    static List<String> list = new ArrayList<>();
    public static List<String> sortPoint(char result[], int k){
        String s = "";
        if(k==result.length){
            for(int i=0;i<result.length;i++){
                s += result[i];
            }
            list.add(s);
            return list;
        }
        for(int i=k;i<result.length;i++){
            //交换
            {char t = result[k];result[k] = result[i];result[i] = t;}
            //递归，下一个数去排列
            sortPoint(result,k+1);
            //交换回来
            {char t = result[k];result[k] = result[i];result[i] = t;}
        }
        return list;
    }

}
