package app.jietuqi.cn.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.HttpLog;

import java.io.File;

import app.jietuqi.cn.constant.Constant;
import app.jietuqi.cn.constant.IntentKey;
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity;
import app.jietuqi.cn.widget.dialog.InviteDialog;
import app.jietuqi.cn.widget.dialog.UpdateDialog;

/**
 * Created by yuanboliu on 2018/8/15.
 * Use for
 */
public class UpdateApkUtil {
	/**
	 * 版本更新的dialog
	 */
	private UpdateDialog mUpdateDialog;
	private AppCompatActivity mContext;
	private File mApkFile;
	public UpdateApkUtil(AppCompatActivity context, ProductFlavorsEntity bean){
		this.mContext = context;
		mUpdateDialog = new  UpdateDialog();
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentKey.ENTITY, bean);
		mUpdateDialog.setArguments(bundle);
		mUpdateDialog.show(mContext.getSupportFragmentManager(), "upgrade");
		new InviteDialog().show(context.getSupportFragmentManager(), "invite");
	}
	public UpdateApkUtil(AppCompatActivity context){
		this.mContext = context;
	}
	public void updataApk(String apkUrl, final  UpdateDialog dialog, final Context context) {//下载回调是在异步里处理的
		FileUtil.RecursionDeleteFile(Constant.APK_PATH);
		EasyHttp.downLoad(apkUrl)
				//EasyHttp.downLoad("http://crfiles2.he1ju.com/0/925096f8-f720-4aa5-86ae-ef30548d2fdc.txt")
				.savePath(Constant.APK_PATH)//默认在：/storage/emulated/0/Android/data/包名/files/1494647767055
				.saveName("wxyxb.apk")//默认名字是时间戳生成的
				.execute(new DownloadProgressCallBack<String>() {
					@Override
					public void update(long bytesRead, long contentLength, boolean done) {
						int progress = (int) (bytesRead * 100 / contentLength);
						HttpLog.e(progress + "% ");
						dialog.setProgress(progress);
						if (done) {

						}
					}
					@Override
					public void onStart() {
						HttpLog.i("======"+Thread.currentThread().getName());
					}

					@Override
					public void onComplete(String path) {
						mApkFile = new File(path);
						installApk(mApkFile);
						dialog.dismiss();
					}

					@Override
					public void onError(final ApiException e) {
						HttpLog.i("======"+Thread.currentThread().getName());
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
	}
	private void installApk(File apk){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
		} else {//Android7.0之后获取uri要用contentProvider
			Uri uri = FileProvider.getUriForFile(mContext, "app.jietuqi.cn.fileprovider", apk);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
