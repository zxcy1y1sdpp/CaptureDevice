package app.jietuqi.cn.ui.qqscreenshot.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import app.jietuqi.cn.wechat.wechatfont.FontCache;

/**
 * 作者： liuyuanbo on 2018/10/21 14:01.
 * 时间： 2018/10/21 14:01
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class QQCharTextView4 extends AppCompatTextView {
    public QQCharTextView4(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public QQCharTextView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public QQCharTextView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("DS-DIGIB.ttf", context);
        setTypeface(customFont);
    }

}
