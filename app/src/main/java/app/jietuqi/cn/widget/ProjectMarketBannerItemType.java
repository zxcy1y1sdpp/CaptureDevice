package app.jietuqi.cn.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ms.banner.holder.BannerViewHolder;

import app.jietuqi.cn.GlideApp;
import app.jietuqi.cn.R;

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
        return mImageView;
    }

    @Override
    public void onBind(Context context, int position, Object data) {
        GlideApp.with(context)
                .load(data.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .error(R.mipmap.loading)
                .fallback(R.mipmap.loading)
                .placeholder(R.mipmap.loading)
                .into(mImageView);
    }
}
