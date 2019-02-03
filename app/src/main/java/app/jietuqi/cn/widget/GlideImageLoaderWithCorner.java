//package app.jietuqi.cn.widget;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.youth.banner.loader.ImageLoader;
//
//import app.jietuqi.cn.util.GlideUtil;
//
///**
// * 作者： liuyuanbo on 2018/12/13 12:50.
// * 时间： 2018/12/13 12:50
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//public class GlideImageLoaderWithCorner extends ImageLoader {
//    private static final long serialVersionUID = -8756585994506298449L;
//
//    @Override
//    public void displayImage(Context context, Object path, ImageView imageView) {
//        /**
//         注意：
//         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
//         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
//         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
//         切记不要胡乱强转！
//         */
//        GlideUtil.displayBannerWithCorner(context, path.toString(), imageView);
//        /*GlideApp.with(context).asDrawable()
//                .load(path)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .thumbnail(0.1f)
//                .error(R.mipmap.loading)
//                .fallback(R.mipmap.loading)
//                .into(imageView);*/
//    }
//}
