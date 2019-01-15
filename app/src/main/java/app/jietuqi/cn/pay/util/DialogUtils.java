package app.jietuqi.cn.pay.util;//package app.jietuqi.cn.pay.util;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.widget.TextView;
//
//import app.jietuqi.cn.R;
///**
// * 作者： liuyuanbo on 2018/11/16 13:27.
// * 时间： 2018/11/16 13:27
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//public class DialogUtils {
//
//    private static Dialog loadingDialog;
//    private static Dialog payResultDialog;
//
//    /**
//     * 显示支付结果dialog
//     */
//    public static boolean showPayResult(Context context, boolean success) {
//        hideAll(context);
//        payResultDialog = new Dialog(context, R.style.Dialog);
//        payResultDialog.setContentView(R.layout.main_payresult_dialog);
//        payResultDialog.getWindow().setBackgroundDrawable(null);
//
//        TextView tv = (TextView) payResultDialog.findViewById(R.id.tv_payresult);
//        if (success) {
//            tv.setText(R.string.main_pay_success);
//            Drawable d = context.getResources().getDrawable(R.drawable.main_payresult_ok);
//            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//            tv.setCompoundDrawables(null, d, null, null);
//        } else {
//            tv.setText(R.string.dialog_pay_fail);
//            Drawable d = context.getResources().getDrawable(R.drawable.main_payresult_error);
//            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//            tv.setCompoundDrawables(null, d, null, null);
//        }
//        payResultDialog.setCanceledOnTouchOutside(true);
//        payResultDialog.setCancelable(true);
//        payResultDialog.show();
//        return true;
//    }
//
//    /**
//     * 隐藏支付结果dialog
//     */
//    public static boolean hidePayResult(Context context) {
//        hideAll(context);
//        return true;
//    }
//
//
//    /**
//     * 显示loading
//     */
//    public static boolean showLoading(Context context) {
//        hideAll(context);
//        loadingDialog = new Dialog(context, R.style.Dialog);
//        loadingDialog.setContentView(R.layout.loading_dialog);
//        loadingDialog.setCanceledOnTouchOutside(false);
//        loadingDialog.setCancelable(false);
//        loadingDialog.show();
//        return true;
//    }
//
//    /**
//     * 隐藏loading
//     */
//    public static boolean hideLoading(Context context) {
//        hideAll(context);
//        return true;
//    }
//
//    private static boolean hideAll(Context context) {
//        if (null != loadingDialog && loadingDialog.isShowing()) {
//            loadingDialog.dismiss();
//        }
//        if (null != payResultDialog && payResultDialog.isShowing()) {
//            payResultDialog.dismiss();
//        }
//        return true;
//    }
//}