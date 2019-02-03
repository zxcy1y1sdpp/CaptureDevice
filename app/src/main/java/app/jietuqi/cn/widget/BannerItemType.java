package app.jietuqi.cn.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.makeramen.roundedimageview.RoundedImageView;
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
public class BannerItemType implements BannerViewHolder<Object> {

    private RoundedImageView mImageView;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
        mImageView = view.findViewById(R.id.bannerItemIv);
        return mImageView;
    }

    @Override
    public void onBind(Context context, int position, Object data) {
        GlideUtil.displayAll(context, data.toString(), mImageView);
    }
}
