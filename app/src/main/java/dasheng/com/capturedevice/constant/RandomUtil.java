package dasheng.com.capturedevice.constant;

import dasheng.com.capturedevice.R;
import dasheng.com.capturedevice.util.TimeUtil;

/**
 * 作者： liuyuanbo on 2018/10/15 12:16.
 * 时间： 2018/10/15 12:16
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class RandomUtil {
    public static final String[] randomNick = {"小猪", "大宝", "简爱", "疯狂！", "是我", "琴儿",
            "花儿和少年", "Lucky", "So Cute", "勿忘心安", "博哥哥",
            "约定", "一定是", "特别的缘分", "才可以", "一路走来",
            "变成了一家人", "他多爱你积分", "就多伤我积分"};
    public static final int[] randomAvatar = {R.mipmap.icon0, R.mipmap.icon1, R.mipmap.icon2,
            R.mipmap.icon3, R.mipmap.icon4, R.mipmap.role_002, R.mipmap.role_003, R.mipmap.role_004,
            R.mipmap.role_005, R.mipmap.role_006, R.mipmap.role_007, R.mipmap.role_008, R.mipmap.role_009,
            R.mipmap.role_020, R.mipmap.role_011, R.mipmap.role_012, R.mipmap.role_013, R.mipmap.role_014 };

    /**
     * 微信零钱明细 -- 收入
     */
    public static final String[] randomWechatChargeEarningDetail = {"微信红包", "群收款", "充值 ", "微信面对面收钱！", "退款", "微信转账"};
    /**
     * 微信零钱明细 -- 支出
     */
    public static final String[] randomWechatChargeExpenditureDetail = {"微信转账 ", "微信红包", "群收款", "付款", "生活缴费", "提现手续费", "提现",
            "100元手机话费-13141135309", "美团订单-1000001809278345463469527", "百度外卖-148635645856548541202",
            "京东-6014689429782", "爱奇艺VIP会员12个月"};
    /**
     * 对方账号（手机号/邮箱） -- 支出
     */
    public static final String[] randomAccounts = {"13141135309 ", "15919819791", "13517352247", "1281274360@qq.com", "250578978@qq.com", "13141135409@163.com"};
    /**
     * 红包编号需要随机生成一个28位长度的数字字符串
     */
    public static final char[] mNumArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /**
     * 获取随机的名称
     * @return
     */
    public static String getRandomNickName(){
        int index = (int) (Math.random() * randomNick.length);
        String random = RandomUtil.randomNick[index];
        return random;
    }
    /**
     * 获取随机的名头像
     * @return
     */
    public static int getRandomAvatar(){
        int index = (int) (Math.random() * randomAvatar.length);
        int random = RandomUtil.randomAvatar[index];
        return random;
    }
    /**
     * 获取随机的收入名称
     * @return
     */
    public static String getRandomChargeEarningDetail(){
        int index = (int) (Math.random() * randomWechatChargeEarningDetail.length);
        String random = RandomUtil.randomWechatChargeEarningDetail[index];
        return random;
    }
    /**
     * 获取随机的支出名称
     * @return
     */
    public static String getRandomChargeExpenditureDetail(){
        int index = (int) (Math.random() * randomWechatChargeExpenditureDetail.length);
        String random = RandomUtil.randomWechatChargeExpenditureDetail[index];
        return random;
    }
    /**
     * 获取随机账号
     * @return
     */
    public static String getRandomAccounts(){
        int index = (int) (Math.random() * randomAccounts.length);
        String random = RandomUtil.randomAccounts[index];
        return random;
    }
    public static String getRandomNum(int size){
        StringBuffer buffer = new StringBuffer(TimeUtil.getNowTimeWithYMD());
        char ch;
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * mNumArray.length);
            ch = mNumArray[index];
            buffer.append(ch);
        }
        return buffer.toString();
    }
}
