package app.jietuqi.cn.wechat.simulator.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import app.jietuqi.cn.wechat.wechatfont.FontCache;

/**
 * 作者： liuyuanbo on 2019/1/15 18:56.
 * 时间： 2019/1/15 18:56
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class RedPointTextView extends AppCompatTextView {
    private Paint roundRectPaint;
    private RectF roundRectF;
    private int height;
    private int roundWidth;

    private Paint textPaint;
    private float textSize;
    private String text;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    public RedPointTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public RedPointTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {
        Typeface customFont = FontCache.getTypeface("HuNan-CC.ttf", context);
        setTypeface(customFont);
        text = "2";
        roundRectPaint = new Paint();
        roundRectPaint.setStyle(Paint.Style.FILL);
        roundRectPaint.setAntiAlias(true);
        roundRectPaint.setColor(Color.parseColor("#F55458"));
        roundRectF = new RectF();

        //设置字体为粗体
        textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
        //实现抗锯齿
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件高度
        height = MeasureSpec.getSize(heightMeasureSpec);
        //定义字体大小，自测了一下，与高度相差6dp的字体大小看着还是挺舒服的
        textSize = 30;
        textPaint.setTextSize(textSize);
        //获取文本宽度
        int textWidth = (int) textPaint.measureText(text);
        //区分画圆的是圆形还是圆角矩形
        if (text.length() > 1) {
            roundWidth = textWidth + height - textWidth / text.length();
        } else {
            roundWidth = height;
        }
        //重新测量控件
        setMeasuredDimension(roundWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画矩形
        canvas.setDrawFilter(paintFlagsDrawFilter);
        roundRectF.set(0, 0, roundWidth, height);
        canvas.drawRoundRect(roundRectF, height / 2, height / 2, roundRectPaint);

        //画字
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLintY = (int) (roundRectF.centerY() - top / 2 - bottom / 2);
        canvas.drawText(text, roundRectF.centerX(), baseLintY, textPaint);
    }

    public void setText(String text) {
        if ("0".equals(text)){
            setVisibility(View.GONE);
        }else {
            setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(text) > 99){
            this.text = "···";
            textPaint.setTextSize(50);
        }else {
            this.text = text;
            textPaint.setTextSize(30);
        }
        invalidate();
        //重走onMeasure
        requestLayout();
    }
}
