package com.rock.jbjianwu;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * XingAijian 2019-02-22
 */

public class FastJsonUtils {
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {

        }
        return t;
    }

    public static<T> List<T> getArray(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
        }
        return list;
    }
}
