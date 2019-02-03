//package app.jietuqi.cn;
//
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.scwang.smartrefresh.layout.api.RefreshHeader;
//import com.scwang.smartrefresh.layout.api.RefreshKernel;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.RefreshState;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//
///**
// * 作者： liuyuanbo on 2019/2/2 17:17.
// * 时间： 2019/2/2 17:17
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//public class CustomRefreshHeader extends FrameLayout implements RefreshHeader {
//    private static final String TAG = "1111111111";
//    private View mContent;
//    private ImageView iv_man;
//    private ImageView iv_goods;
//    private TextView tv_remain;  //刷新提示文字
//    private LayoutParams mContentLayoutParams;
//    private LayoutParams iv_manLayoutParams;
//    private AnimationDrawable drawable;
//
//    public CustomRefreshHeader(Context context) {
//        super(context, null);
//    }
//
//    public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        mContent = LayoutInflater.from(context).inflate(R.layout.refresh_head, null);
//        iv_man = mContent.findViewById(R.id.iv_man);
//        iv_goods = mContent.findViewById(R.id.iv_goods);
//        tv_remain = mContent.findViewById(R.id.tv_remain);
//        addView(mContent);
//    }
//    @NonNull
//    @Override
//    public View getView() {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
//        return null;
//    }
//
//    @Override
//    public void setPrimaryColors(int... colors) {
//
//    }
//
//    @Override
//    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
//
//    }
//
//    @Override
//    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
//        return 0;
//    }
//
//    @Override
//    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
//
//    }
//
//    @Override
//    public boolean isSupportHorizontalDrag() {
//        return false;
//    }
//
//    @Override
//    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
//
//    }
//   /* private static final String TAG = "1111111111";
//    private View mContent;
//    private ImageView iv_man;
//    private ImageView iv_goods;
//    private TextView tv_remain;  //刷新提示文字
//    private LayoutParams mContentLayoutParams;
//    private LayoutParams iv_manLayoutParams;
//    private AnimationDrawable drawable;
//
//    public CustomRefreshHeader(Context context) {
//        super(context, null);
//    }
//
//    public CustomRefreshHeader(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        mContent = LayoutInflater.from(context).inflate(R.layout.refresh_head, null);
//        iv_man = mContent.findViewById(R.id.iv_man);
//        iv_goods = mContent.findViewById(R.id.iv_goods);
//        tv_remain = mContent.findViewById(R.id.tv_remain);
//        addView(mContent);
//    }
//
//    @Override
//    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
//        Log.d(TAG, "onPullingDown: percent :" + percent + "   headHeight  " + headerHeight);
//        mContentLayoutParams = (LayoutParams) mContent.getLayoutParams();
//        mContentLayoutParams.topMargin = (int) (-(1 - percent) * headerHeight);
//        mContent.setLayoutParams(mContentLayoutParams);
//
//        if (percent<1){
//            iv_goods.setScaleY(percent);
//            iv_goods.setScaleX(percent);
//            iv_man.setScaleY(percent);
//            iv_man.setScaleX(percent);
//            iv_manLayoutParams = ((LayoutParams) iv_man.getLayoutParams());
//            iv_manLayoutParams.rightMargin= (int) (200*(1-percent));
//            iv_man.setLayoutParams(iv_manLayoutParams);
//            tv_remain.setText("下拉刷新....");
//        }else {
//            tv_remain.setText("释放刷新....");
//        }
//
//
//    }
//
//    @Override
//    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
//        Log.d(TAG, "onReleasing: percent  :" + percent);
//
//        mContentLayoutParams = (LayoutParams) mContent.getLayoutParams();
//        mContentLayoutParams.topMargin = (int) (-(1 - percent) * headerHeight);
//        mContent.setLayoutParams(mContentLayoutParams);
//
//        if (percent<1){
//            iv_goods.setScaleY(percent);
//            iv_goods.setScaleX(percent);
//            iv_man.setScaleY(percent);
//            iv_man.setScaleX(percent);
//        }else {
//            tv_remain.setText("刷新中....");
//        }
//
//    }
//
//    @NonNull
//    @Override
//    public View getView() {
//        Log.d(TAG, "getView: ");
//        return this;
//    }
//
//
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
////        Log.d(TAG, "getSpinnerStyle: ");
//        return null;
//    }
//
//    @Override
//    public void setPrimaryColors(int... colors) {
////        Log.d(TAG, "setPrimaryColors: ");
//    }
//
//    @Override
//    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContent.getLayoutParams();
//        layoutParams.topMargin = -height;
//        Log.d(TAG, "onInitialized: ");
//    }
//
//    @Override
//    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
//        iv_man.setBackgroundResource(R.drawable.running);
//        drawable = (AnimationDrawable) iv_man.getBackground();
//        if (!drawable.isRunning())
//            drawable.start();
//        iv_goods.setVisibility(GONE);
//    }
//
//    @Override
//    public int onFinish(RefreshLayout layout, boolean success) {
//        if (drawable.isRunning()) {
//            drawable.stop();
//            drawable=null;
//        }
//        iv_goods.setVisibility(VISIBLE);
//        iv_man.setBackgroundResource(R.drawable.person);
//        return 0;
//    }
//
//    @Override
//    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        Log.d(TAG, "onStateChanged: ");
//    }*/
//}
