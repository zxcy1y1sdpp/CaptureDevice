package app.jietuqi.cn.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import app.jietuqi.cn.wechat.wechatfont.FontCache;

/**
 * 作者： liuyuanbo on 2018/12/18 13:41.
 * 时间： 2018/12/18 13:41
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class NoPaddingTextView extends AppCompatTextView {
    private final Paint mPaint = new Paint();

    private final Rect mBounds = new Rect();

    public NoPaddingTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public NoPaddingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        final String text = calculateTextParams();

        final int left = mBounds.left;
        final int bottom = mBounds.bottom;
        mBounds.offset(-mBounds.left, -mBounds.top);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getCurrentTextColor());
        canvas.drawText(text, -left, mBounds.bottom - bottom, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateTextParams();
        setMeasuredDimension(mBounds.width() + 1, -mBounds.top + 2);
    }

    private String calculateTextParams() {
        final String text = getText().toString();
        final int textLength = text.length();
        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, textLength, mBounds);
        if (textLength == 0) {
            mBounds.right = mBounds.left;
        }
        return text;
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("WeChatLqNumber.ttf", context);
        setTypeface(customFont);
    }
}
