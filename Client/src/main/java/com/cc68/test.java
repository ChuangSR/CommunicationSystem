package com.cc68;

import com.alibaba.fastjson2.JSON;
import com.cc68.beans.MessageBean;
import com.cc68.utils.MessageUtil;
import com.cc68.utils.SqlUtil;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        HashMap<String,String> hashMap = new HashMap<>();
        temp(hashMap);
        System.out.println(hashMap.get("adas"));
    }

    public static void temp(HashMap<String,String> hashMap){
        hashMap.put("adas","gadgag");
    }

}
