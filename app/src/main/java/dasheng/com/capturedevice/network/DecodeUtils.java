package dasheng.com.capturedevice.network;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by 刘远博 on 2016/12/26.
 */

public class DecodeUtils<T> {

    public static DecodeUtils getInstance(){
        return new DecodeUtils<>();
    }

    public static String decodeJson(String json){
        try {
            json = AESOperatorUtils.getInstance().decrypt(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }
    public T decodeDatas(String json, Type type, Class clz) {
        Gson gson = new Gson();
        T t = (T) getObject(clz);
        t = gson.fromJson(json, type);//把JSON格式的字符串转为obj 
        return t;
    }

    public Object getObject(Class c) {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
