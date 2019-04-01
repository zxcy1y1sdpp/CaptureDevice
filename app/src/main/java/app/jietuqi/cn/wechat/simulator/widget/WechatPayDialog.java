package app.jietuqi.cn.wechat.simulator.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import app.jietuqi.cn.R;

/**
 * 作者： liuyuanbo on 2019/1/26 10:13.
 * 时间： 2019/1/26 10:13
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class WechatPayDialog extends Dialog {
    int[] images = { R.mipmap.wechat_pay_loading_1,R.mipmap.wechat_pay_loading_2,R.mipmap.wechat_pay_loading_3};
    // Message传递标志
    int SIGN = 17;
    // 照片索引
    int num = 0;
    private ImageView loadingIv;
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SIGN) {
                loadingIv.setImageResource(images[num++]);
                if (num >= images.length) {
                    num = 0;
                }
            }
        }
    };

    public WechatPayDialog(Context context) {
        super(context, R.style.CustomDialog);
        initView();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = SIGN;
                handler.sendMessage(msg);
            }
        }, 0, 250);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(WechatPayDialog.this.isShowing())
                    WechatPayDialog.this.dismiss();
                break;
        }
        return true;
    }

    private void initView(){
        setContentView(R.layout.dialog_wechat_pay);
        loadingIv = findViewById(R.id.dialogLoadingIv);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.8f;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }
}
