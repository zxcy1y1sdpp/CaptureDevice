package app.jietuqi.cn.widget;

/**
 * 作者： liuyuanbo on 2019/1/3 13:41.
 * 时间： 2019/1/3 13:41
 * 邮箱： 972383753@qq.com
 * 用途： 使TextView中不同大小字体顶部对齐
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
public class TopSpan extends ReplacementSpan {

    private float fontSizePx;    //px
    private int color;

    public TopSpan(float fontSizePx,int color) {
        this.fontSizePx = fontSizePx;
        this.color = color;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return (int) p.measureText(text.toString());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        // 此处重新计算y坐标，使字体顶部对齐
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) - (bottom + top)), p);
//        canvas.drawText(text.toString(), x, y, p);
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(fontSizePx);   //设定字体大小, sp转换为px
        paint.setColor(color);
        return paint;
    }
}
