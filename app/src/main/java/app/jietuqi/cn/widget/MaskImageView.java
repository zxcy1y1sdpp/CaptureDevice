package app.jietuqi.cn.widget;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 作者： liuyuanbo on 2019/2/21 13:18.
 * 时间： 2019/2/21 13:18
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class MaskImageView extends AppCompatImageView {
    private boolean touchEffect = true;
    public final float[] BG_PRESSED = new float[] { 1, 0, 0, 0, -50, 0, 1, 0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0 };
    public final float[] BG_NOT_PRESSED = new float[] { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

    public MaskImageView(Context context) {
        super(context);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
        updateView(pressed);
        super.setPressed(pressed);
    }

    /**
     * 根据是否按下去来刷新bg和src
     *
     * @param pressed
     */
    private void updateView(boolean pressed){
        //如果没有点击效果
        if( !touchEffect ){
            return;
        }
        if( pressed ){
            /**
             * 通过设置滤镜来改变图片亮度
             */
            this.setDrawingCacheEnabled(true);
            this.setColorFilter( new ColorMatrixColorFilter(BG_PRESSED) ) ;
            //此为src，背景用getBackground()
            this.getDrawable().setColorFilter( new ColorMatrixColorFilter(BG_PRESSED) );
        }else{

            this.setColorFilter( new ColorMatrixColorFilter(BG_NOT_PRESSED) ) ;
            this.getDrawable().setColorFilter( new ColorMatrixColorFilter(BG_NOT_PRESSED) );
        }
    }
}
