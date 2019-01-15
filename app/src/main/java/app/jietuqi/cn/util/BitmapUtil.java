package app.jietuqi.cn.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者： liuyuanbo on 2018/11/27 11:32.
 * 时间： 2018/11/27 11:32
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class BitmapUtil {
    // 饿汉式
    private static BitmapUtil instance = new BitmapUtil();

    private BitmapUtil(){}

    public static BitmapUtil getInstance(){
        return instance;
    }

    /*
     *    get image from network
     *    @param [String]imageURL
     *    @return [BitMap]image
     */
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
