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

public class AlipayTextView3 extends AppCompatTextView {
    public AlipayTextView3(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public AlipayTextView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public AlipayTextView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("score.ttf", context);
        setTypeface(customFont);
    }

}
