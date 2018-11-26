package com.zhouyou.http.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhouyou.http.R;

/**
 *
 * @Description loading
 * @author
 * @time 2014-11-12
 */
public class ProgressUtils {

    public static Dialog mProgressDialog;
    public static View view;
    /**
     * @param message
     * @param context
     */
    public static void showProgressDialog(String message, Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_wechat_loading, null);
        TextView title_view = view.findViewById(R.id.title);
        if (TextUtils.isEmpty(message)){
            title_view.setVisibility(View.GONE);
        }else {
            title_view.setText(message);
        }
        if (mProgressDialog == null){
            mProgressDialog = new Dialog(context, R.style.MyDialog);
        }
        mProgressDialog.setContentView(view);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
    /**
     * @param message
     * @param context
     */
    public static void showProgressDialog(String message, Context context, boolean cancel) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_wechat_loading, null);
        TextView title_view = view.findViewById(R.id.title);
        if (TextUtils.isEmpty(message)){
            title_view.setVisibility(View.GONE);
        }else {
            title_view.setText(message);
        }
        if (mProgressDialog == null){
            mProgressDialog = new Dialog(context, R.style.MyDialog);
        }
        mProgressDialog.setContentView(view);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(cancel);
        mProgressDialog.show();
    }
    public static void cancleProgressDialog() {
        try {
            if (mProgressDialog != null){
                mProgressDialog.dismiss();
                mProgressDialog = null;
                view = null;
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    public static Dialog getDialog(){
        return mProgressDialog;
    }
}