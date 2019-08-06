package com.soto.utils;

import com.alibaba.fastjson.JSON;
import com.soto.entity.Order;

/**
 * 解决反序列化失败问题
 */
public class JsonConvertUtils {

    public static String convertObjectToJSON(Order order) {
        return JSON.toJSONString(order);

    }

}