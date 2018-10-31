package dasheng.com.capturedevice.wechat.wechatfont;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * 作者： liuyuanbo on 2018/10/21 14:00.
 * 时间： 2018/10/21 14:00
 * 邮箱： 972383753@qq.com
 * 用途： 这将缓存字体，同时最小化对assets文件夹的访问次数
 */

public class FontCache {
    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }
        return typeface;
    }
}
