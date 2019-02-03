package app.jietuqi.cn.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 作者： liuyuanbo on 2019/1/24 21:07.
 * 时间： 2019/1/24 21:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class NeverCrashGridLayoutManager extends GridLayoutManager {
    public NeverCrashGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NeverCrashGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NeverCrashGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("problem", "meet a IOOBE in RecyclerView");
        }
    }
}
