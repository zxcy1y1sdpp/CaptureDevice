package app.jietuqi.cn.callback;

import android.support.v7.widget.RecyclerView;

/**
 * 作者： liuyuanbo on 2018/11/27 23:30.
 * 时间： 2018/11/27 23:30
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class FabScrollListener extends RecyclerView.OnScrollListener {
    private HideScrollListener listener;
    private static final int THRESHOLD = 20;
    private int distance = 0;
    private boolean visible = true;//是否可见

    public FabScrollListener(HideScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (distance > THRESHOLD && visible) {
            //隐藏动画
            visible = false;
            listener.onHide();
            distance = 0;
        } else if (distance < -20 && !visible) {
            //显示动画
            visible = true;
            listener.onShow();
            distance = 0;
        }
        if (visible && dy > 0 || (!visible && dy < 0)) {
            distance += dy;
        }
    }
}
