package dasheng.com.capturedevice.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import dasheng.com.capturedevice.R;

/**
 * 作者： liuyuanbo on 2018/10/25 13:34.
 * 时间： 2018/10/25 13:34
 * 邮箱： 972383753@qq.com
 * 用途： 邀请的分享弹框
 */

public class InviteDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ChangeRoleDialog;
        View view = inflater.inflate(R.layout.dialog_invite, container, false);
        return view;
    }
}
