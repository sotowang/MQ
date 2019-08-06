package com.soto.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soto.entity.Order;

/**
 * 解决反序列化失败问题
 */
public class JsonConvertUtils {

    public static Order convertJSONToObject(JSONObject json) {
        return JSONObject.toJavaObject(json, Order.class);
    }


}