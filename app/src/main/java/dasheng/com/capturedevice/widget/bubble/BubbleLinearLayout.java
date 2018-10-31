package dasheng.com.capturedevice.widget.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import dasheng.com.capturedevice.R;

/**
 * 作者： liuyuanbo on 2018/10/12 15:33.
 * 时间： 2018/10/12 15:33
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class BubbleLinearLayout extends LinearLayout {
    private BubbleDrawable bubbleDrawable;
    private float mArrowWidth;
    private float mAngle;
    private float mAngleLeftTop;
    private float mAngleRightTop;
    private float mAngleRightBottom;
    private float mAngleLeftBottom;
    private float mArrowHeight;
    private float mArrowPosition;
    private BubbleDrawable.ArrowLocation mArrowLocation;
    private int bubbleColor;
    private String color;
    private boolean mArrowCenter;
    public BubbleLinearLayout(Context context) {
        super(context);
        initView(null);
    }

    public BubbleLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }


    private void initView(AttributeSet attrs){
        if (attrs != null){
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BubbleView);
            mArrowWidth = array.getDimension(R.styleable.BubbleView_arrowWidth, BubbleDrawable.Builder.DEFAULT_ARROW_WITH);
            mArrowHeight = array.getDimension(R.styleable.BubbleView_arrowHeight, BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT);
            mAngle = array.getDimension(R.styleable.BubbleView_angle, BubbleDrawable.Builder.DEFAULT_ANGLE);
            mAngleLeftTop = array.getDimension(R.styleable.BubbleView_angleTopLeft, BubbleDrawable.Builder.DEFAULT_ANGLE);
            mAngleRightTop = array.getDimension(R.styleable.BubbleView_angleTopRight, BubbleDrawable.Builder.DEFAULT_ANGLE);
            mAngleLeftBottom = array.getDimension(R.styleable.BubbleView_angleBottomLeft, BubbleDrawable.Builder.DEFAULT_ANGLE);
            mAngleRightBottom = array.getDimension(R.styleable.BubbleView_angleBottomRight, BubbleDrawable.Builder.DEFAULT_ANGLE);
            mArrowPosition = array.getDimension(R.styleable.BubbleView_arrowPosition, BubbleDrawable.Builder.DEFAULT_ARROW_POSITION);
            bubbleColor = array.getColor(R.styleable.BubbleView_bubbleColor, BubbleDrawable.Builder.DEFAULT_BUBBLE_COLOR);
            int location = array.getInt(R.styleable.BubbleView_arrowLocation, 0);
            mArrowLocation = BubbleDrawable.ArrowLocation.mapIntToValue(location);
            mArrowCenter = array.getBoolean(R.styleable.BubbleView_arrowCenter, false);
            array.recycle();
        }
    }
    public void setBubbleColor(String color){
        this.color = color;
        if (null != bubbleDrawable){
            bubbleDrawable.color = color;
        }
        requestLayout();
        invalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0){
            setUp(w, h);
        }
    }

    private void setUp(int left, int right, int top, int bottom){
        if (right < left || bottom < top)
            return;
        RectF rectF = new RectF(left, top, right, bottom);
        bubbleDrawable = new BubbleDrawable.Builder()
                .rect(rectF)
                .arrowLocation(mArrowLocation)
                .bubbleType(BubbleDrawable.BubbleType.COLOR)
                .angleLeftBottom(mAngleLeftBottom)
                .angleLeftTop(mAngleLeftTop)
                .angleRightBottom(mAngleRightBottom)
                .angleRightTop(mAngleRightTop)
//                .angle(mAngle)
                .arrowHeight(mArrowHeight)
                .arrowWidth(mArrowWidth)
                .arrowPosition(mArrowPosition)
                .bubbleColor(bubbleColor)
                .arrowCenter(mArrowCenter)
                .build();
        bubbleDrawable.color = color;
    }

    private void setUp(int width, int height){
        setUp(getPaddingLeft(),  width - getPaddingRight(), getPaddingTop(), height - getPaddingBottom());
        setBackgroundDrawable(bubbleDrawable);
    }

    public void resetBubbleColor (int bubbleColor) {
        RectF rectF = new RectF(getPaddingLeft(), getWidth() - getPaddingRight(), getPaddingTop(), getHeight() - getPaddingBottom());
        bubbleDrawable = new BubbleDrawable.Builder()
                .rect(rectF)
                .arrowLocation(mArrowLocation)
                .bubbleType(BubbleDrawable.BubbleType.COLOR)
                .angleLeftBottom(mAngleLeftBottom)
                .angleLeftTop(mAngleLeftTop)
                .angleRightBottom(mAngleRightBottom)
                .angleRightTop(mAngleRightTop)
//                .angle(mAngle)
                .arrowHeight(mArrowHeight)
                .arrowWidth(mArrowWidth)
                .arrowPosition(mArrowPosition)
                .bubbleColor(bubbleColor)
                .arrowCenter(mArrowCenter)
                .build();
        post(new Runnable() {
            @Override
            public void run() {
                setUp(getWidth(), getHeight());
                setBackgroundDrawable(bubbleDrawable);
                postInvalidate();
            }
        });
    }
}
