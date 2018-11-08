package dasheng.com.capturedevice.network;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yuanboliu on 2018/8/23.
 * Use for
 */
public abstract class BaseObserver<T> implements Observer<APIResponse<T>> {
//public abstract class BaseObserver<T> implements Observer<APIResponse<T>> {

	@Override
	public void onSubscribe(Disposable d) {
		onRequestStart();
	}

	@Override
	public void onNext(APIResponse<T> apiResponse) {
		if (apiResponse.isSuccess()) {
			try {
				onSuccees(apiResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				onCodeError(apiResponse);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        onRequestEnd();
	}
	@Override
	public void onError(Throwable e) {
//        Log.w(TAG, "onError: ", );这里可以打印错误信息
		onRequestEnd();
		try {
			if (e instanceof ConnectException || e instanceof TimeoutException
					|| e instanceof NetworkErrorException
					|| e instanceof UnknownHostException) {
				onFailure(e, true);
			} else {
				onFailure(e, false);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onComplete() {
		Log.e("onComplete", "onError: ");
	}

	/**
	 * 返回成功
	 *
	 * @param t
	 * @throws Exception
	 */
	protected abstract void onSuccees(APIResponse<T> t) throws Exception;

	/**
	 * 返回成功了,但是code错误
	 *
	 * @param t
	 * @throws Exception
	 */
	protected void onCodeError(APIResponse<T> t) throws Exception {
	}

	/**
	 * 返回失败
	 *
	 * @param e
	 * @param isNetWorkError 是否是网络错误
	 * @throws Exception
	 */
	protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

	protected void onRequestStart() {
        showProgressDialog();
        Log.e("retrofit", "请求开始");
	}

	protected void onRequestEnd() {
        Log.e("retrofit", "请求结束");
		closeProgressDialog();
	}

	public void showProgressDialog() {
//		ProgressDialog.show(mContext, false, "请稍后");
	}

	public void closeProgressDialog() {
//		ProgressDialog.cancle();
	}

}