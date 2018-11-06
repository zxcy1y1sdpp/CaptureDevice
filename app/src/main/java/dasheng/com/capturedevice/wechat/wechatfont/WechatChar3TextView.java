package dasheng.com.capturedevice.wechat.wechatfont;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 作者： liuyuanbo on 2018/10/21 14:01.
 * 时间： 2018/10/21 14:01
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class WechatChar3TextView extends AppCompatTextView {
    public WechatChar3TextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public WechatChar3TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public WechatChar3TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("WeChatSansStd-Medium.ttf", context);
        setTypeface(customFont);
    }

}
