package app.jietuqi.cn.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.zlzq.utils.ScreenUtil;
import com.github.promeg.pinyinhelper.Pinyin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.jietuqi.cn.R;
import app.jietuqi.cn.entity.ProvinceEntity;
import app.jietuqi.cn.ui.entity.WechatUserEntity;

/**
 * 作者： liuyuanbo on 2018/10/19 17:01.
 * 时间： 2018/10/19 17:01
 * 邮箱： 972383753@qq.com
 * 用途： 一些乱七八糟的通用的东西，不想放入父类中
 */

public class OtherUtil {
    /**
     * 微信两个蓝色按钮来回切换的状态
     * @param selectedTv 选中的按钮
     * @param unSelectedTv 未选中的按钮
     */
    public static void changeWechatTwoBtnBg(Context context, TextView selectedTv, TextView unSelectedTv){
        selectedTv.setBackgroundResource(R.drawable.blue_round_cornor);
        unSelectedTv.setBackgroundResource(R.drawable.blue_solid);
        selectedTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        unSelectedTv.setTextColor(ContextCompat.getColor(context, R.color.wechatBlue));
    }
    /**
     * 微信生成页面中的预览按钮
     * @param previewBtn 预览按钮
     * @param canPreview 是否可以预览
     */
    public static void changeWechatPreviewBtnBg(Context context, Button previewBtn, boolean canPreview){
        if (canPreview){
            previewBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.green_round_cornor));
            previewBtn.setEnabled(true);
            previewBtn.setClickable(true);
        }else{
            previewBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.green_round_cornor_no));
            previewBtn.setEnabled(false);
            previewBtn.setClickable(false);
        }
    }

    /**
     * 开关按钮的方法
     * @param on
     * @param imageView
     */
    public static void onOrOff(boolean on, ImageView imageView){
        if (on){
            imageView.setImageResource(R.mipmap.on);
        }else{
            imageView.setImageResource(R.mipmap.off);
        }
    }

    /**
     * 微信两个蓝色按钮来回切换的状态
     * @param selectedTv 选中的按钮
     * @param unSelectedTv 未选中的按钮
     */
    public static void changeReoprtProblemBtnBg(Context context, TextView selectedTv, TextView unSelectedTv){
        selectedTv.setBackgroundResource(R.drawable.blue_solid1);
        unSelectedTv.setBackgroundResource(R.drawable.gray_solid1);
        selectedTv.setTextColor(ContextCompat.getColor(context, R.color.wechatBlue));
        unSelectedTv.setTextColor(ContextCompat.getColor(context, R.color.overallGray));
    }

    /**
     * 格式化成支付宝的数字显示方式
     * @param price
     * @return
     */
    public static String formatPrice(Object price) {
        DecimalFormat formater = new DecimalFormat("#,##0.00");
        // keep 2 decimal places
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(3);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return formater.format(Double.parseDouble(String.valueOf(price)));
//
//
//        float distanceValue = Math.round((Double.parseDouble(String.valueOf(price))/10f))/100f;
//        DecimalFormat decimalFormat =newfun DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//        String distanceString = decimalFormat.format(distanceValue) + km;
//        return distanceString;
    }

    /**
     * 获取手续费
     * 0.1%
     * @param money
     * @return
     */
    public static String getServiceCharge(String money){
        if (Float.parseFloat(money) < 104){
            return "0.10";
        }else{
            String charge = new BigDecimal(Float.parseFloat(money) * 0.001).setScale(2, RoundingMode.UP).toString();
            return charge;
        }
    }
    /**
     * 解析省的数据
     */
    public static ProvinceEntity parseProvinceJson(Context context) {
        String filName = "province.json";
        ProvinceEntity province = null;
        if (province == null) {
            String jsonData_city = getJson(filName, context);
            Gson gson = new Gson();
            province = gson.fromJson(jsonData_city, new TypeToken<ProvinceEntity>() {}.getType());
        }
        return province;
    }
    /**
     *  从asset路径下读取对应文件转String输出
     *  @return
     */
    public static String getJson(String fileName, Context context)  {
        InputStreamReader isr;
        StringBuilder builder = new StringBuilder();
        try {
            isr = new InputStreamReader(context.getAssets().open(fileName),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 获取edittext中的内容
     * @param editText
     * @return
     */
    public static String getContent(EditText editText){
        String content = editText.getText().toString();
        return content.trim();
    }
    public static <T> String getContent(T t) {
        String type = t.getClass().getSimpleName();

        String content = "";
        try {
            switch (type) {
                case "AppCompatEditText":
                    content = ((EditText)t).getText().toString().trim();
                    break;
                case "AppCompatTextView":
                    content = ((TextView)t).getText().toString().trim();
                    break;
            }
        } catch (Exception e) {
            content = "";
            e.printStackTrace();
        }
        return content;
    }
    /**
     * 判断edittext中输入多少个字符
     * @param editText
     * @return
     */
    public static int getContentLength(EditText editText){
        String content = editText.getText().toString().trim();
        int length;
        if (TextUtils.isEmpty(content)){
            return 0;
        }else {
            length = content.length();
            return length;
        }
    }

    /**
     * 复制到粘贴板
     * @param context
     * @param content
     */
    public static void copy(Context context, String content){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.showShort(context, "复制成功");
    }

    /**
     * 提取真正的网络地址
     * @param content
     * @return
     */
    public static String readRealPath(String content){
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(content);
        String link = "";
        if (matcher.find()) {
            link = matcher.group(0);
        }
        return link;
    }
    /**
     * 处理过滤emoji图像
     */
    public static String removeNonBmpUnicode(String nickName){
        String str = "";
        str = str.replace("[^\\u0000-\\uFFFF]", "");
        return str;
    }
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 昵称转拼音
     * @param nickName
     * @return
     */
    public static String transformPinYin(String nickName) {
        StringBuffer buffer = new StringBuffer();
        String firstChar = "";
        for (int i = 0; i < nickName.length(); i++) {
            buffer.append(Pinyin.toPinyin(nickName.charAt(i)));
            if (i == 0){
                firstChar = buffer.toString().substring(0, 1);
                if (!firstChar.toUpperCase().matches("[A-Z]")){
                    return "#";
                }
            }
        }
        return buffer.toString();
    }
    /**
     * 获取拼音的第一个字母
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(String pinyin) {
        String firstPinYin = pinyin.substring(0, 1);
        return firstPinYin;
    }

    public static class PinyinComparator implements Comparator<WechatUserEntity> {
        @Override
        public int compare(WechatUserEntity o1, WechatUserEntity o2) {
            if ("@".equals(o1.pinyinNickName) || "#".equals(o2.pinyinNickName)) {
                return -1;
            } else if ("#".equals(o1.pinyinNickName) || "@".equals(o2.pinyinNickName)) {
                return 1;
            } else {
                return o1.pinyinNickName.compareTo(o2.pinyinNickName);
            }
        }
    }

    public static void setWechatVoiceLength(Context context, LinearLayout layout, int length){
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        double miniPercent = 0.1646;
        if (length <= 2){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
        }else if (length < 10){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (length - 2) * 0.0216));
        }else if (length >= 10 && length < 20){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (8) * 0.0216));
        }else if (length >= 20 && length < 30){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (9) * 0.0216));
        }else if (length >= 30 && length < 40){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (10) * 0.0216));
        }else if (length >= 40 && length < 50){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (11) * 0.0216));
        }else if (length >= 50 && length < 60){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (12) * 0.0216));
        }else if (length >= 60){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (13) * 0.0216));
        }
        layout.setLayoutParams(params);
    }
    public static void setAlipayVoiceLength(Context context, LinearLayout layout, int length){
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        double miniPercent = 0.22413793;
        if (length <= 2){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
        }else if (length < 10){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (length - 2) * 0.0216));
        }else if (length >= 10 && length < 20){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (8) * 0.0216));
        }else if (length >= 20 && length < 30){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (9) * 0.0216));
        }else if (length >= 30 && length < 40){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (10) * 0.0216));
        }else if (length >= 40 && length < 50){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (11) * 0.0216));
        }else if (length >= 50 && length < 60){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (12) * 0.0216));
        }else if (length >= 60){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * (miniPercent + (13) * 0.0216));
        }
        layout.setLayoutParams(params);
    }
    public static void setQQVoiceLength(Context context, ViewGroup layout, int length){
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        double miniPercent = 0.00431965;
        double shortStPercent = 0.28638498;//最短的百分比
        double lengthMoreThan10 = ScreenUtil.INSTANCE.getScreenWidth(context) * 0.0237581;//超过10秒的时候每一段录音都要加一个这个长度
        double shortLength = ScreenUtil.INSTANCE.getScreenWidth(context) * shortStPercent;//一秒的时候的长度
        if (length < 10){
            double stepLength = (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
            stepLength = stepLength * length;
            params.width = (int) (stepLength + shortLength);
        }else if (length >= 10 && length < 30){
            double stepLength = (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
            stepLength = stepLength * length;
            stepLength += lengthMoreThan10;
            params.width = (int) (stepLength + shortLength);
        }else if (length >= 30 && length <= 59){
            double stepLength = (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
            stepLength = stepLength * length;
            stepLength += lengthMoreThan10;
            miniPercent = 0.00215983;
            stepLength = stepLength + (length - 30) * (ScreenUtil.INSTANCE.getScreenWidth(context) * miniPercent);
            params.width = (int) (stepLength + shortLength);
        }else if(length >= 60){
            params.width = (int) (ScreenUtil.INSTANCE.getScreenWidth(context) * 0.59395248);
        }

        layout.setLayoutParams(params);
    }
}
