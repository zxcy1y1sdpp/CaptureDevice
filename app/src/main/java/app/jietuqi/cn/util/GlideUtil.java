package app.jietuqi.cn.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bm.zlzq.utils.ScreenUtil;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;

import app.jietuqi.cn.App;
import app.jietuqi.cn.GlideApp;
import app.jietuqi.cn.R;
import app.jietuqi.cn.ui.entity.WechatUserEntity;

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
	public static void displayAll(Context context, Object id, ImageView imageView){
		GlideApp.with(context)
				.load(id)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.placeholder(R.mipmap.loading).dontAnimate()
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.into(imageView);
	}
	public static void display(Context context, String path, ImageView imageView){
		GlideApp.with(context)
				.load(path)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.placeholder(R.mipmap.loading)
				.into(imageView);
	}
	public static void displayBannerWithCorner(Context context, String url, ImageView imageView){
		GlideApp.with(context)
				.load(url)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.placeholder(R.mipmap.loading)
				.apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
				.into(imageView);
	}

	/**
	 * 展示头像相关
	 * @param context
	 * @param id
	 * @param imageView
	 */
	public static void displayHead(Context context, Object id, ImageView imageView){
		GlideApp.with(context)
				.load(id)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.thumbnail(0.1f)
				.error(R.drawable.head_default)
				.fallback(R.drawable.head_default)
				.placeholder(R.drawable.head_default)
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
				.placeholder(R.mipmap.loading)
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
				.placeholder(R.mipmap.loading)
				.override(width)
				.into(imageView);
	}
	/**
	 * 加载sdcard中的图片文件
	 * @param context
	 * @param file
	 * @param imageView
	 */
	public static void displayQQ(Context context, File file, ImageView imageView){
		int width = ScreenUtil.INSTANCE.getScreenWidth(context) / 8 * 3;
		GlideApp.with(context)
				.load(file)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.placeholder(R.mipmap.loading)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.override(width)
				.into(imageView);
	}
	public static void displayAlipay(Context context, File file, ImageView imageView){
		int width = ScreenUtil.INSTANCE.getScreenWidth(context) / 9 * 3;
		GlideApp.with(context)
				.load(file)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.placeholder(R.mipmap.loading)
				.override(width)
				.into(imageView);
	}
	public static void displayAlipay2(Context context, File file, ImageView imageView){
		int width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) / 8.8 * 3);
		GlideApp.with(context)
				.load(file)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.thumbnail(0.1f)
				.error(R.mipmap.loading)
				.fallback(R.mipmap.loading)
				.placeholder(R.mipmap.loading)
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
				.placeholder(R.mipmap.loading)
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
	public static void display(Context context, WechatUserEntity entity, ImageView imageView){
		if (entity.resAvatar <= 0){
			display(context, entity.avatarFile, imageView);
		}else {
			display(context, entity.resAvatar, imageView);
		}
	}
	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除Glide图片缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
	public static void clearViews(Context context, ImageView view){
		GlideApp.with(context).clear(view);
	}
	/** * 清除内存缓存. */
	public static void clearMemoryCache(Context context){
		// This method must be called on the main thread.
		System.gc();
		// Glide.get(context).clearMemory();
		GlideApp.get(context).clearMemory();
	}
	/**－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ 删除磁盘缓存 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－*/
	public static void clearDiskCache(){
		try {
			GlideApp.get(App.Companion.getInstance()).clearDiskCache();
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}
	}
}
