package app.jietuqi.cn.callback;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
    private int mTranslate = 0;
    private boolean visible = true;//是否可见

    public FabScrollListener(HideScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //获取滑动距离，，通过布局管理器
        //1.获得视图的第一条木的下标
        //2.根据下标获得view条目,,,在获得条目的高度
        //3.下标*条目高度-可见视图距离顶部的高度
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        int i4 = (position) * itemHeight - firstVisiableChildView.getTop();
        Log.i("jibbb","==="+i4);
        /*if(i4>600){
            sousuo.setBackgroundColor(getResources().getColor(R.color.fenleilvbeijing));
        }else{
            sousuo.setBackgroundColor(getResources().getColor(R.color.touming));
        }*/
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        /*int firstItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstItemPosition == 0){
            if (dy > 0){//上滑
                if (dy > 100){
                    listener.onShowTitle(mTranslate);
                    Log.e("上滑", mTranslate+"");
                }
            }else {//下滑
                if (mTranslate < -100) {
                    Log.e("下滑",mTranslate+"");
                    listener.onHideTitle(mTranslate);
                }
            }
        }
*/
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
        /*LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        int i4 = (position) * itemHeight - firstVisiableChildView.getTop();
        if (i4 > 600){
            listener.onShowTitle(mTranslate);
        }else {
            listener.onHideTitle(mTranslate);
        }
        Log.i("jibbb","==="+i4);*/
//        Log.e("distance --------", getDistance(recyclerView) + "");
    }
}
