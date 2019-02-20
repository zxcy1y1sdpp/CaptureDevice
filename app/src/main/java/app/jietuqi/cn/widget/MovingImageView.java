package app.jietuqi.cn.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * 作者： liuyuanbo on 2019/2/20 15:05.
 * 时间： 2019/2/20 15:05
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class MovingImageView extends AppCompatImageView {
    public MovingImageView(Context context) {
        super(context);
        this.setOnTouchListener(shopCarSettleTouch);
    }

    public MovingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(shopCarSettleTouch);
    }

    public MovingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(shopCarSettleTouch);
    }
    private View.OnTouchListener shopCarSettleTouch = new View.OnTouchListener() {
        int rawY1, rawX1, rawY, rawX; //手指落下和抬起的位置 用来判断用户是移动图标还是点击事件
        int rr, ll; //当用户放开手指的时候  ，通过位置判断 让View自动靠边
        int lastX, lastY; // view的的当前位置  用来计算更新view的位置

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int ea = event.getAction();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;//需要减掉图片的高度
            switch (ea) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                    lastY = (int) event.getRawY();
                    rawX1 = (int) event.getRawX();
                    rawY1 = (int) event.getRawY();
                case MotionEvent.ACTION_MOVE:
                    //event.getRawX();获得移动的位置
                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    int l = v.getLeft() + dx;
                    int b = v.getBottom() + dy;
                    int r = v.getRight() + dx;
                    int t = v.getTop() + dy;
                    //下面判断移动是否超出屏幕
                    if (l < 0) {
                        l = 0;
                        r = l + v.getWidth();
                    }
                    if (t < 30) {
                        t = 30;
                        b = t + v.getHeight();
                    }
                    if (r > screenWidth) {
                        r = screenWidth;
                        l = r - v.getWidth();
                    }
                    if (b > screenHeight - 200) {
                        b = screenHeight - 200;
                        t = b - v.getHeight();
                    }
                    v.layout(l, t, r, b);
                    lastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
                    lastY = (int) event.getRawY();
                    v.postInvalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (rawX1 != lastX && rawY1 != lastY){
                        return true;
                    }
                    /*this.rawX = (int) event.getRawX();
                    this.rawY = (int) event.getRawY();
                    if (v.getLeft() < (screenWidth / 2 + v.getWidth() / 2)) {
                        ll = 20;
                        rr = ll + v.getWidth();
                    }
                    if (v.getRight() > (screenWidth / 2 + v.getWidth() / 2)) {
                        rr = screenWidth - 20;
                        ll = rr - v.getWidth();
                    }
                    v.layout(ll, v.getTop(), rr, v.getBottom());
                    v.postInvalidate();
                    if (Math.abs(this.rawX - rawX1) > 20 || Math.abs(this.rawY - rawY1) > 20) {
                        return true;
                    }*/
                    break;
            }
            return false;
        }
    };
}
