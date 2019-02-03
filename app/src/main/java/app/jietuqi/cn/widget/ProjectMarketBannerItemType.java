package app.jietuqi.cn.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ms.banner.holder.BannerViewHolder;

import app.jietuqi.cn.R;
import app.jietuqi.cn.util.GlideUtil;

/**
 * Created by songwenchao
 * on 2018/5/17 0017.
 * <p>
 * 类名
 * 需要 --
 * 可以 --
 */
public class ProjectMarketBannerItemType implements BannerViewHolder<Object> {

    private ImageView mImageView;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_project_market_banner, null);
        mImageView = view.findViewById(R.id.bannerItemIv);
        // 返回mImageView页面布局
        /*mImageView = new RoundedImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);*/
        return mImageView;
    }

    @Override
    public void onBind(Context context, int position, Object data) {
        GlideUtil.displayAll(context, data.toString(), mImageView);
    }
}
