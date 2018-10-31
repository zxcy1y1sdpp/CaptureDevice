package dasheng.com.capturedevice.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.util.EventBusUtil
import kotlinx.android.synthetic.main.dialog_change_role.*

/**
 * @author lyb
 * * created at 17/6/29 下午7:45
 * * be used for 联系客服的弹框
 */
class ChangeRoleDialog : DialogFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cancelTv ->{}
            R.id.okTv ->{
                EventBusUtil.postSticky(nickNameEt.text.toString())
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val window = dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.attributes.windowAnimations = R.style.ChangeRoleDialog
        val view = inflater.inflate(R.layout.dialog_change_role, container, false)
        view.findViewById<TextView>(R.id.cancelTv).setOnClickListener(this)
        view.findViewById<TextView>(R.id.okTv).setOnClickListener(this)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            dialog.window.setLayout((dm.widthPixels * 0.75).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    //	@Override
    //	public void onClick(View v) {
    //		switch (v.getId()){
    //			case R.id.server_chat:
    //				AppUtils.chatWithServerWithoutGoods(getActivity());
    //				break;
    //			case R.id.server_phone:
    //				callNeeds();
    //				break;
    //			case R.id.server_wechat:
    //				//获取剪贴板管理器：
    //				ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
    //				// 创建普通字符型ClipData
    //				ClipData mClipData = ClipData.newPlainText("Label", "zulzuq_kf");
    //				// 将ClipData内容放到系统剪贴板里。
    //				cm.setPrimaryClip(mClipData);
    //
    //				Intent intent =new Intent();
    //				ComponentName cmp =new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
    //				intent.setAction(Intent.ACTION_MAIN);
    //				intent.addCategory(Intent.CATEGORY_LAUNCHER);
    //				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //				intent.setComponent(cmp);
    //				startActivity(intent);
    //
    //				Toast.makeText(getActivity(), "成功复制客服微信号", Toast.LENGTH_SHORT).show();
    ////				ServerDialogPermissionsDispatcher.callNeedsWithCheck(ChangeRoleDialog.this);
    //				break;
    //			case R.id.server_cancle:
    //				break;
    //			default:break;
    //		}
    //		dismiss();
    //	}
    //
    //	void callNeeds() {
    //		Intent intent = new Intent(Intent.ACTION_DIAL);
    //		Uri data = Uri.parse("tel:" + "4001058192");
    //		intent.setData(data);
    //		startActivity(intent);
    //	}
}
