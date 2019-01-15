package app.jietuqi.cn.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bm.zlzq.utils.ScreenUtil;

/**
 * 作者： liuyuanbo on 2018/12/17 17:57.
 * 时间： 2018/12/17 17:57
 * 邮箱： 972383753@qq.com
 * 用途： 拖动的textview
 */
public class MoveTextView extends AppCompatTextView {
    private static final String TAG = "MoveTextView";

    private int lastX = 0;
    private int lastY = 0;
    private static int screenWidth = 720;
    private static int screenHeight = 1280;
    public MoveTextView(Context context) {
        super(context);
        screenWidth = ScreenUtil.INSTANCE.getScreenWidth(getContext());
        screenHeight = ScreenUtil.INSTANCE.getScreenHeight(getContext());
    }

    public MoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth = ScreenUtil.INSTANCE.getScreenWidth(getContext());
        screenHeight = ScreenUtil.INSTANCE.getScreenHeight(getContext());
    }

    public MoveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = ScreenUtil.INSTANCE.getScreenWidth(getContext());
        screenHeight = ScreenUtil.INSTANCE.getScreenHeight(getContext());
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx =(int)event.getRawX() - lastX;
                int dy =(int)event.getRawY() - lastY;

                left = getLeft() + dx;
                top = getTop() + dy;
                right = getRight() + dx;
                bottom = getBottom() + dy;
                if(left < 0){
                    left = 0;
                    right = left + getWidth();
                }
                if(right > screenWidth){
                    right = screenWidth;
                    left = right - getWidth();
                }
                if(top < 0){
                    top = 0;
                    bottom = top + getHeight();
                }
                if(bottom > screenHeight){
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                break;
            default:
                break;
        }
        return true;
    }
}
