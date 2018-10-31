package dasheng.com.capturedevice.widget.bubble;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * 作者： liuyuanbo on 2018/10/12 15:31.
 * 时间： 2018/10/12 15:31
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class BubbleDrawable extends Drawable {
    private RectF mRect;
    private Path mPath = new Path();
    private BitmapShader mBitmapShader;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mArrowWidth;
    public String color;
//    private float mAngle;

    private float mAngleLeftTop;
    private float mAngleRightTop;
    private float mAngleRightBottom;
    private float mAngleLeftBottom;
    private float mArrowHeight;
    private float mArrowPosition;
    private int bubbleColor;
    private Bitmap bubbleBitmap;
    private ArrowLocation mArrowLocation;
    private BubbleType bubbleType;
    private boolean mArrowCenter;

    private BubbleDrawable(Builder builder) {
        this.mRect = builder.mRect;
//        this.mAngle = builder.mAngle;
        this.mAngleLeftBottom = builder.mAngleLeftBottom;
        this.mAngleRightBottom = builder.mAngleRightBottom;
        this.mAngleLeftTop = builder.mAngleLeftTop;
        this.mAngleRightTop = builder.mAngleRightTop;
        this.mArrowHeight = builder.mArrowHeight;
        this.mArrowWidth = builder.mArrowWidth;
        this.mArrowPosition = builder.mArrowPosition;
        this.bubbleColor = builder.bubbleColor;
        this.bubbleBitmap = builder.bubbleBitmap;
        this.mArrowLocation = builder.mArrowLocation;
        this.bubbleType = builder.bubbleType;
        this.mArrowCenter = builder.arrowCenter;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        setUp(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    private void setUpPath(ArrowLocation mArrowLocation, Path path) {
        switch (mArrowLocation) {
            case LEFT:
                setUpLeftPath(mRect, path);
                break;
            case RIGHT:
                setUpRightPath(mRect, path);
                break;
            case TOP:
                setUpTopPath(mRect, path);
                break;
            case BOTTOM:
                setUpBottomPath(mRect, path);
                break;
        }
    }

    private void setUp(Canvas canvas) {
        switch (bubbleType) {
            case COLOR:
                if (TextUtils.isEmpty(color)) {
                    mPaint.setColor(bubbleColor);
                }else {
                    mPaint.setColor(Color.parseColor(color));
                }
                break;
            case BITMAP:
                if (bubbleBitmap == null)
                    return;
                if (mBitmapShader == null) {
                    mBitmapShader = new BitmapShader(bubbleBitmap,
                            Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                }
                mPaint.setShader(mBitmapShader);
                setUpShaderMatrix();
                break;
        }
        setUpPath(mArrowLocation, mPath);
        canvas.drawPath(mPath, mPaint);
    }

    private void setUpLeftPath(RectF rect, Path path) {

        if (mArrowCenter) {
            mArrowPosition = (rect.bottom - rect.top) / 2 - mArrowWidth / 2;
        }

        path.moveTo(mArrowWidth + rect.left + mAngleRightTop, rect.top);
        path.lineTo(rect.width() - mAngleRightTop, rect.top);
        //右上
        path.arcTo(new RectF(rect.right - mAngleRightTop, rect.top, rect.right, mAngleRightTop + rect.top), 270, 90);
//        path.arcTo(new RectF(rect.right - mAngle, rect.top, rect.right, mAngle + rect.top), 270, 90);


        path.lineTo(rect.right, rect.bottom - mAngleRightBottom);
        //右下
        path.arcTo(new RectF(rect.right - mAngleRightBottom, rect.bottom - mAngleRightBottom, rect.right, rect.bottom), 0, 90);


        path.lineTo(rect.left + mArrowWidth + mAngleLeftBottom, rect.bottom);
        //左下
        path.arcTo(new RectF(rect.left + mArrowWidth, rect.bottom - mAngleLeftBottom, mAngleLeftBottom + rect.left + mArrowWidth, rect.bottom), 90, 90);



        path.lineTo(rect.left + mArrowWidth, mArrowHeight + mArrowPosition);
        path.lineTo(rect.left, mArrowPosition + mArrowHeight / 2);
        path.lineTo(rect.left + mArrowWidth, mArrowPosition);
        path.lineTo(rect.left + mArrowWidth, rect.top + mAngleLeftTop);
        //左上
        path.arcTo(new RectF(rect.left + mArrowWidth, rect.top, mAngleLeftTop + rect.left + mArrowWidth, mAngleLeftTop + rect.top), 180, 90);
        path.close();
    }
    private void setUpRightPath(RectF rect, Path path) {

        if (mArrowCenter) {
            mArrowPosition = (rect.bottom - rect.top) / 2 - mArrowWidth / 2;
        }
        //右上
        path.moveTo(rect.left + mAngleRightTop, rect.top);
        path.lineTo(rect.width() - mAngleRightTop - mArrowWidth, rect.top);
        path.arcTo(new RectF(rect.right - mAngleRightTop - mArrowWidth, rect.top, rect.right - mArrowWidth, mAngleRightTop + rect.top), 270, 90);
        //右下
        path.lineTo(rect.right - mArrowWidth, mArrowPosition);
        path.lineTo(rect.right, mArrowPosition + mArrowHeight / 2);
        path.lineTo(rect.right - mArrowWidth, mArrowPosition + mArrowHeight);
        path.lineTo(rect.right - mArrowWidth, rect.bottom - mAngleRightBottom);
        path.arcTo(new RectF(rect.right - mAngleRightBottom - mArrowWidth, rect.bottom - mAngleRightBottom, rect.right - mArrowWidth, rect.bottom), 0, 90);
        //左下
        path.lineTo(rect.left + mArrowWidth, rect.bottom);
        path.arcTo(new RectF(rect.left, rect.bottom - mAngleLeftBottom, mAngleLeftBottom + rect.left, rect.bottom), 90, 90);
        //左上
        path.arcTo(new RectF(rect.left, rect.top, mAngleLeftTop + rect.left, mAngleLeftTop + rect.top), 180, 90);
        path.close();
    }
    private void setUpTopPath(RectF rect, Path path) {

        if (mArrowCenter) {
            mArrowPosition = (rect.right - rect.left) / 2 - mArrowWidth / 2;
        }
        //右上
        path.moveTo(rect.left + Math.min(mArrowPosition, mAngleRightTop), rect.top + mArrowHeight);
        path.lineTo(rect.left + mArrowPosition, rect.top + mArrowHeight);
        path.lineTo(rect.left + mArrowWidth / 2 + mArrowPosition, rect.top);
        path.lineTo(rect.left + mArrowWidth + mArrowPosition, rect.top + mArrowHeight);
        path.lineTo(rect.right - mAngleRightTop, rect.top + mArrowHeight);
        path.arcTo(new RectF(rect.right - mAngleRightTop, rect.top + mArrowHeight, rect.right, mAngleRightTop + rect.top + mArrowHeight), 270, 90);
        //右下
        path.lineTo(rect.right, rect.bottom - mAngleRightBottom);
        path.arcTo(new RectF(rect.right - mAngleRightBottom, rect.bottom - mAngleRightBottom, rect.right, rect.bottom), 0, 90);
        //左下
        path.lineTo(rect.left + mAngleLeftBottom, rect.bottom);
        path.arcTo(new RectF(rect.left, rect.bottom - mAngleLeftBottom, mAngleLeftBottom + rect.left, rect.bottom), 90, 90);
        //左上
        path.lineTo(rect.left, rect.top + mArrowHeight + mAngleLeftTop);
        path.arcTo(new RectF(rect.left, rect.top + mArrowHeight, mAngleLeftTop + rect.left, mAngleLeftTop + rect.top + mArrowHeight), 180, 90);
        path.close();
    }

    private void setUpBottomPath(RectF rect, Path path) {
        if (mArrowCenter) {
            mArrowPosition = (rect.right - rect.left) / 2 - mArrowWidth / 2;
        }
        //右上
        path.moveTo(rect.left + mAngleRightTop, rect.top);
        path.lineTo(rect.width() - mAngleRightTop, rect.top);
        path.arcTo(new RectF(rect.right - mAngleRightTop, rect.top, rect.right, mAngleRightTop + rect.top), 270, 90);
        //右下
        path.lineTo(rect.right, rect.bottom - mArrowHeight - mAngleRightBottom);
        path.arcTo(new RectF(rect.right - mAngleRightBottom, rect.bottom - mAngleRightBottom - mArrowHeight, rect.right, rect.bottom - mArrowHeight), 0, 90);
        //左下
        path.lineTo(rect.left + mArrowWidth + mArrowPosition, rect.bottom - mArrowHeight);
        path.lineTo(rect.left + mArrowPosition + mArrowWidth / 2, rect.bottom);
        path.lineTo(rect.left + mArrowPosition, rect.bottom - mArrowHeight);
        path.lineTo(rect.left + Math.min(mAngleLeftBottom, mArrowPosition), rect.bottom - mArrowHeight);
        path.arcTo(new RectF(rect.left, rect.bottom - mAngleLeftBottom - mArrowHeight, mAngleLeftBottom + rect.left, rect.bottom - mArrowHeight), 90, 90);
        //左上
        path.lineTo(rect.left, rect.top + mAngleLeftTop);

        path.arcTo(new RectF(rect.left, rect.top, mAngleLeftTop + rect.left, mAngleLeftTop + rect.top), 180, 90);
        path.close();
    }

    private void setUpShaderMatrix() {
        Matrix mShaderMatrix = new Matrix();
        mShaderMatrix.set(null);
        int mBitmapWidth = bubbleBitmap.getWidth();
        int mBitmapHeight = bubbleBitmap.getHeight();
        float scaleX = getIntrinsicWidth() / (float) mBitmapWidth;
        float scaleY = getIntrinsicHeight() / (float) mBitmapHeight;
        mShaderMatrix.postScale(scaleX, scaleY);
        mShaderMatrix.postTranslate(mRect.left, mRect.top);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mRect.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mRect.height();
    }

    public static class Builder {
        public static float DEFAULT_ARROW_WITH = 25;
        public static float DEFAULT_ARROW_HEIGHT = 25;
        public static float DEFAULT_ANGLE = 20;
        public static float DEFAULT_ARROW_POSITION = 50;
        public static int DEFAULT_BUBBLE_COLOR = Color.RED;
        private RectF mRect;
        private float mArrowWidth = DEFAULT_ARROW_WITH;
        //        private float mAngle = DEFAULT_ANGLE;
        private float mAngleLeftTop = DEFAULT_ANGLE;
        private float mAngleRightTop = DEFAULT_ANGLE;
        private float mAngleRightBottom = DEFAULT_ANGLE;
        private float mAngleLeftBottom = DEFAULT_ANGLE;
        private float mArrowHeight = DEFAULT_ARROW_HEIGHT;
        private float mArrowPosition = DEFAULT_ARROW_POSITION;
        private int bubbleColor = DEFAULT_BUBBLE_COLOR;
        private Bitmap bubbleBitmap;
        private BubbleType bubbleType = BubbleType.COLOR;
        private ArrowLocation mArrowLocation = ArrowLocation.LEFT;
        private boolean arrowCenter;

        public Builder rect(RectF rect) {
            this.mRect = rect;
            return this;
        }

        public Builder arrowWidth(float mArrowWidth) {
            this.mArrowWidth = mArrowWidth;
            return this;
        }

        //        public Builder angle(float mAngle) {
//            this.mAngle = mAngle * 2;
//            return this;
//        }
        public Builder angleLeftTop(float leftTop) {
            this.mAngleLeftTop = leftTop * 2;
            return this;
        }
        public Builder angleRightTop(float rightTop) {
            this.mAngleRightTop = rightTop * 2;
            return this;
        }
        public Builder angleLeftBottom(float leftBottom) {
            this.mAngleLeftBottom = leftBottom * 2;
            return this;
        }
        public Builder angleRightBottom(float rigntBottom) {
            this.mAngleRightBottom = rigntBottom * 2;
            return this;
        }

        public Builder arrowHeight(float mArrowHeight) {
            this.mArrowHeight = mArrowHeight;
            return this;
        }

        public Builder arrowPosition(float mArrowPosition) {
            this.mArrowPosition = mArrowPosition;
            return this;
        }

        public Builder bubbleColor(int bubbleColor) {
            this.bubbleColor = bubbleColor;
            bubbleType(BubbleType.COLOR);
            return this;
        }

        public Builder bubbleBitmap(Bitmap bubbleBitmap) {
            this.bubbleBitmap = bubbleBitmap;
            bubbleType(BubbleType.BITMAP);
            return this;
        }

        public Builder arrowLocation(ArrowLocation arrowLocation) {
            this.mArrowLocation = arrowLocation;
            return this;
        }

        public Builder bubbleType(BubbleType bubbleType) {
            this.bubbleType = bubbleType;
            return this;
        }

        public Builder arrowCenter(boolean arrowCenter) {
            this.arrowCenter = arrowCenter;
            return this;
        }

        public BubbleDrawable build() {
            if (mRect == null) {
                throw new IllegalArgumentException("BubbleDrawable Rect can not be null");
            }
            return new BubbleDrawable(this);
        }
    }

    public enum ArrowLocation {
        LEFT(0x00),
        RIGHT(0x01),
        TOP(0x02),
        BOTTOM(0x03);

        private int mValue;

        ArrowLocation(int value) {
            this.mValue = value;
        }

        public static ArrowLocation mapIntToValue(int stateInt) {
            for (ArrowLocation value : ArrowLocation.values()) {
                if (stateInt == value.getIntValue()) {
                    return value;
                }
            }
            return getDefault();
        }

        public static ArrowLocation getDefault() {
            return LEFT;
        }

        public int getIntValue() {
            return mValue;
        }
    }

    public enum BubbleType {
        COLOR,
        BITMAP
    }
}
