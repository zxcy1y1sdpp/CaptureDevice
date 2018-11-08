package dasheng.com.capturedevice.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bm.zlzq.utils.ScreenUtil;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.InputStream;

import dasheng.com.capturedevice.GlideApp;
import dasheng.com.capturedevice.R;
import dasheng.com.capturedevice.database.table.WechatUserTable;

/**
 * Created by yuanboliu on 17/7/25.
 * Use for Glide图片加载
 */

public class GlideUtil{

	/**
	 * 加载本地图片
	 * @param context
	 * @param id
	 * @param imageView
	 */
	public static void display(Context context, int id, ImageView imageView){
		GlideApp.with(context)
				.load(id)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.into(imageView);
	}
	public static void display(Context context, String id, ImageView imageView){
		GlideApp.with(context)
				.load(id)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.into(imageView);
	}
	public static void display2(Context context, int id, ImageView imageView){
		int width = ScreenUtil.INSTANCE.getScreenWidth(context) / 7 * 3;
		GlideApp.with(context)
				.load(id)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.override(width)
				.into(imageView);
	}
	/**
	 * 加载sdcard中的图片文件
	 * @param context
	 * @param file
	 * @param imageView
	 */
	public static void display(Context context, File file, ImageView imageView){
		int width = ScreenUtil.INSTANCE.getScreenWidth(context) / 7 * 3;
		GlideApp.with(context)
				.load(file)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.override(width)
				.into(imageView);
	}
	public static void displayFile(Context context, File file, ImageView imageView){
		GlideApp.with(context)
				.load(file)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.into(imageView);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context 设备上下文
	 * @param resId 资源ID
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
	}

	/**
	 * 加载二进制图片
	 * @param context
	 * @param resource：二进制图片
	 * @param imageView
	 */
	public static void display(Context context, byte resource, ImageView imageView){
		GlideApp.with(context)
				.load(resource)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.into(imageView);
	}

	/**
	 * 区分是资源文件图片加载还是sdcard中的图片加载
	 * @param context
	 * @param entity
	 * @param imageView
	 */
	public static void display(Context context, WechatUserTable entity, ImageView imageView){
		if (entity.resAvatar <= 0){
			display(context, entity.avatar, imageView);
		}else {
			display(context, entity.resAvatar, imageView);
		}
	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 加载正常图片 （没有对尺寸进行重写的）－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	/**
//	 * 加载正常商品图片（或者租来租趣首页等的图片，没有圆角等特殊处理的图片）
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayNormalImage(Context context, String url, ImageView imageView){
//		try{
//			GlideApp.with(context)
//					.load(url)
////					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.thumbnail(0.1f)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//
//	}
//	public static void displayNormalImage(Context context, int url, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(url)
//					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.thumbnail(0.1f)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 加载非正常图片 （重写了尺寸或者添加圆角的图片，或者两者都有的图片）－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	/**
//	 * 加载租来租趣商品列表的图片
//	 * @param context
//	 * @param url
//	 * @param imageView
//	 */
//	public static void displayGoodsImage(Context context, String url, ImageView imageView){
//		try {
//			int spec = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * 0.3);
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.centerCrop()
//					.override(spec, spec)
//					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.thumbnail(0.1f)
//					.placeholder(R.mipmap.loading).error(R.mipmap.loading).into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载二手首页商品图片
//	 * 尺寸是宽度的0.35倍，圆角30度
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayHomeImage(final Context context, String url, final ImageView imageView, int spec){
//		try {
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.centerCrop()
//					.dontAnimate()
//					.placeholder(R.mipmap.loading)
//					.thumbnail(0.1f)
//					.diskCacheStrategy( DiskCacheStrategy.RESOURCE)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 加载横向滑动的图片，每屏幕展示三个
//	 * @param context
//	 * @param url
//	 * @param imageView
//	 */
//	public static void disPlayHorizontal3Image(Context context, String url, ImageView imageView){
//		try {
//			int spec = (int) (ScreenUtil.INSTANCE.getScreenWidth() * 0.5 * 0.6);
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.dontAnimate()
//					.placeholder(R.mipmap.loading)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.override(spec, spec)
//					.thumbnail(0.1f)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayImageUsedMainHorizantal(Context context, String url, ImageView imageView){
//		try {
//			int spec = (int) (ScreenUtil.INSTANCE.getScreenWidth() * 0.35);
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.dontAnimate()
//					.placeholder(R.mipmap.loading)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.override(spec, spec)
//					.thumbnail(0.1f)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 加载正常商品图片（或者租来租趣首页等的图片，没有圆角等特殊处理的图片）
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayMemberCardImage(Context context, String url, ImageView imageView){
//		try {
//			int screenWidth = ScreenUtil.INSTANCE.getScreenWidth();
//			int height = screenWidth / 10 * 3;
//			int width = screenWidth / 6 * 4;
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.thumbnail(0.1f)
//					.fitCenter()
//					.override(width, height)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载正常商品图片（或者租来租趣首页等的图片，没有圆角等特殊处理的图片）
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayRecommendToyImage(Context context, String url, ImageView imageView, int width, int height){
//		try {
//			int allWidth = ScreenUtil.INSTANCE.getScreenWidth() / 2;
//			int height2 = (int)  (allWidth * 0.82);
//			int width2 = (int) (height * 1.6);
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.thumbnail(0.1f)
//					.fitCenter()
//					.override(width2, height2)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载圆角图片重写宽高规格的图片
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 * @param corner:圆角的度数
//	 * @param spec:图片的宽高规格
//	 */
//	public static void displayCornerWithSpecImage(final Context context, String url, final ImageView imageView, final int corner, int spec){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.dontAnimate()
//					.load(StringUtils.getPhotoUrl(url))//网络URL
//					.error(R.mipmap.loading)//异常图片
//					.placeholder(R.mipmap.loading)//占位图片
//					.fallback(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.override(spec, spec)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(new BitmapImageViewTarget(imageView) {
//				@Override
//				protected void setResource(Bitmap resource) {
//					RoundedBitmapDrawable cornorBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//					cornorBitmapDrawable.setCornerRadius(corner);
//					cornorBitmapDrawable.setAntiAlias(true);
//					imageView.setImageDrawable(cornorBitmapDrawable);
//				}
//			});
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载圆角图片没有重写宽高规格的图片
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 * @param corner:圆角的度数
//	 */
//	public static void displayCornerWithoutSpecImage(Context context, String url, ImageView imageView, float corner){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.dontAnimate()
//					.load(StringUtils.getPhotoUrl(url))//网络URL
//					.error(R.mipmap.loading)//异常图片
//					.placeholder(R.mipmap.loading)//占位图片
//					.fallback(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(new CircularBitmapImageViewTarget(context,imageView, corner));
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 加载头像图片 （重写了尺寸或者添加圆角的图片，或者两者都有的图片）－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	/**
//	 * 加载圆角头像
//	 * @param context
//	 * @param url:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayCircleImage(final Context context, String url, final ImageView imageView){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.load(StringUtils.getPhotoUrl(url))//网络URL
//					.error(R.mipmap.my_head)//异常图片
//					.placeholder(R.mipmap.my_head)//占位图片
//					.fallback(R.mipmap.my_head)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(new BitmapImageViewTarget(imageView) {
//				@Override
//				protected void setResource(Bitmap resource) {
//					RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//					circularBitmapDrawable.setCircular(true);
//					imageView.setImageDrawable(circularBitmapDrawable);
//				}
//			});
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载圆角头像
//	 * @param context
//	 * @param id:图片路径
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayCircleImage(final Context context, int id, final ImageView imageView){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.load(id)//网络URL
//					.error(R.mipmap.my_head)//异常图片
//					.placeholder(R.mipmap.my_head)//占位图片
//					.fallback(R.mipmap.my_head)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(new BitmapImageViewTarget(imageView) {
//				@Override
//				protected void setResource(Bitmap resource) {
//					RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//					circularBitmapDrawable.setCircular(true);
//					imageView.setImageDrawable(circularBitmapDrawable);
//				}
//			});
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 加载File图片 （没有对尺寸进行重写的）－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//
//	/**
//	 * 加载圆角30度的文件图片
//	 * @param context
//	 * @param file:文件
//	 * @param imageView
//	 */
//	public static void displayFileWithCornerImage(final Context context, File file, final ImageView imageView){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.load(file)//网络URL
//					.error(R.mipmap.loading)//异常图片
//					.placeholder(R.mipmap.loading)//占位图片
//					.fallback(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(new CircularBitmapImageViewTarget(context,imageView, 30));
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载文件图片
//	 * @param context
//	 * @param file:文件
//	 * @param imageView
//	 */
//	public static void displayFileImage(final Context context, File file, final ImageView imageView){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.load(file)//网络URL
//					.error(R.mipmap.loading)//异常图片
//					.placeholder(R.mipmap.loading)//占位图片
//					.fallback(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.centerCrop()
//					.thumbnail(0.1f);//当url为空时，显示图片
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayFileImageWithListener(final Context context, File file, final ImageView imageView){
//		try {
//			RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
//					.asBitmap()//指定Bitmap类型的RequestBuilder
//					.listener(new RequestListener<Bitmap>() {
//						@Override
//						public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//							EventBus.getDefault().postSticky(new EventBusGlideBean(false));
//							return false;
//						}
//
//						@Override
//						public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//							EventBus.getDefault().postSticky(new EventBusGlideBean(true));
//							return false;
//						}
//					})
//					.dontAnimate()
//					.load(file)//网络URL
//					.error(R.mipmap.loading)//异常图片
//					.fallback(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//			//在RequestBuilder<Bitmap> 中使用自定义的ImageViewTarget
//			bitmapRequestBuilder.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 加载聊天表情 （没有对尺寸进行重写的）－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	/**
//	 * 加载聊天表情的图片
//	 * @param context
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayEmojiconImage(Context context, int id, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(id)
//					.placeholder(R.drawable.ease_default_expression)
//					.thumbnail(0.1f)
//					.error(R.drawable.ease_default_expression)
//					.fallback(R.drawable.ease_default_expression)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 加载聊天表情的图片
//	 * @param context
//	 * @param imageView:显示图片的控件
//	 */
//	public static void displayEmojiconImage(Context context, String url, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(url)
//					.placeholder(R.drawable.ease_default_expression)
//					.thumbnail(0.1f)
//					.error(R.drawable.ease_default_expression)
//					.fallback(R.drawable.ease_default_expression)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//
//	public static void displayBlurImage(Context context, String url, ImageView imageView){
//		try {
//			Glide.with(context).load(StringUtils.getPhotoUrl(url)).apply(RequestOptions.bitmapTransform(new GlideBlurformation(context))).into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除Glide图片缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void clearViews(Context context, View view){
//		try {
//			GlideApp.with(context).clear(view);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除磁盘缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void clearDiskCache(){
//		try {
//			GlideApp.get(ZLZQApplication.getInstance()).clearDiskCache();
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除内存缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void clearMemory(){
//		try {
//			GlideApp.get(ZLZQApplication.getInstance()).clearMemory();
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除单个页面缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void clearSinglePageMemory(Context context){
//		GlideApp.get(context).clearMemory();
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 暂停 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void pause(Context context){
//		try {
//			GlideApp.with(context).pauseRequestsRecursive();
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 开始加载啊 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void startGlide(Context context){
//		try {
//			GlideApp.with(context).resumeRequestsRecursive();
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 停止 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void stop(Context context){
//		try {
//			GlideApp.with(context).onStop();
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 低内存处理 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
//	public static void onLowMemory(Context context){
//		GlideApp.with(context).onLowMemory();
//	}
//
//	public static void displayNormalImage2(Context context, String url, ImageView imageView, int width, int height){
//		try {
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.thumbnail(0.1f)
//					.placeholder(R.mipmap.loading)
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.override(width, height)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayNormalImage2(Context context, String url, final ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.listener(new RequestListener<Drawable>() {
//						@Override
//						public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//							EventBus.getDefault().postSticky(new EventBusGlideBean(false));
//							return false;
//						}
//
//						@Override
//						public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//							EventBus.getDefault().postSticky(new EventBusGlideBean(true));
//							return false;
//						}
//					})
//					.dontAnimate()
//					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//					.skipMemoryCache(false)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayCouponImage(Context context, String url, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.dontAnimate()
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.error(R.mipmap.loading)
//					.fallback(R.mipmap.loading)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayCouponNormalImage(Context context, String url, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(StringUtils.getPhotoUrl(url))
//					.placeholder(R.mipmap.coupon_default)
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.thumbnail(0.1f)
//					.error(R.mipmap.coupon_default)
//					.fallback(R.mipmap.coupon_default)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
//	public static void displayCouponNormalImage(Context context, int url, ImageView imageView){
//		try {
//			GlideApp.with(context)
//					.load(url)
//					.placeholder(R.mipmap.coupon_default)
//					.diskCacheStrategy(DiskCacheStrategy.ALL)
//					.thumbnail(0.1f)
//					.error(R.mipmap.coupon_default)
//					.fallback(R.mipmap.coupon_default)
//					.into(imageView);
//		}catch (IllegalArgumentException e){
//			e.printStackTrace();
//		}
//	}
}
