package app.jietuqi.cn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * @作者：liuyuanbo
 * @时间：2018/10/23
 * @邮箱：972383753@qq.com
 * @用途：
 */
public class FunItemDecoration extends RecyclerView.ItemDecoration{
	/**
	 * 自定义分割线的高度
	 */
//	public int mLeftSpase = 20;
//	public int mTopSpase = 60;
//	public int mRightSpase = 20;
//	public int mBottomSpase = 60;


	//我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
	public static final int[] ATRRS  = new int[]{
			android.R.attr.listDivider
	};

	public FunItemDecoration(Context context) {
		final TypedArray ta = context.obtainStyledAttributes(ATRRS);
		ta.recycle();
	}

	/**
	 * padding
	 * @param outRect
	 * @param view
	 * @param parent
	 * @param state
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		//画横线，就是往下偏移一个分割线的高度
		outRect.left = 0;
		outRect.top = 4;
		outRect.right = 0;
		// Add top margin only for the first item to avoid double space between items
		outRect.bottom = 4;
	}
}

