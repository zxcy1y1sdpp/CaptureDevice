package app.jietuqi.cn.constant;

import app.jietuqi.cn.R;
import app.jietuqi.cn.util.TimeUtil;

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
     * 随机的网络图片 -- 用于测试
     */
    public static final String[] randomNetPics = new String[]{"http://www.jietuqi.cn/uploads/picture/20181121/6f41237c4da78075ee8175b13da7c0d6.jpeg"//800*599
            ,"http://tupian.qqjay.com/u/2017/1118/1_162252_2.jpg"//800*599
            ,"http://tupian.qqjay.com/u/2017/1118/1_162252_4.jpg"//800*640
            ,"http://tupian.qqjay.com/u/2017/1118/1_162252_3.jpg"//800*599
            ,"http://img.daimg.com/uploads/allimg/111006/1-111006233019140.jpg"//970*644
            ,"http://bpic.wotucdn.com/18/79/66/18796653-b48a73194dac2f95a0c2626414470f85-1.jpg"//1024*768
            ,"http://pic12.nipic.com/20110226/5498846_084758425314_2.jpg"//1024*768
            ,"http://tupian.qqjay.com/u/2017/1208/3_143331_8.jpg"//1680*2520
            ,"http://pic5.nipic.com/20100104/2572038_000048625018_2.jpg"//1024*740
            ,"http://img2.imgtn.bdimg.com/it/u=2398464402,4254570216&fm=11&gp=0.jpg"//500*312
            ,"http://imgsrc.baidu.com/imgad/pic/item/962bd40735fae6cde1dfa14205b30f2442a70fad.jpg"//1000*666
            ,"http://pic1.win4000.com/wallpaper/5/58943656ca48a.jpg"//1920*1080
            ,"http://imgsrc.baidu.com/imgad/pic/item/730e0cf3d7ca7bcbd462a8b1b4096b63f624a817.jpg"//1024*683
            ,"http://imgsrc.baidu.com/imgad/pic/item/4034970a304e251fd79aba66ad86c9177f3e533a.jpg"//678*1024
            ,"http://pic4.nipic.com/20090802/592840_202448093_2.jpg"//778*1201
            ,"http://att.0xy.cn/attachment/Mon_1301/20_17777_47c24b46a47ec0a.jpg?250"//1000*1432
    };
    /**
     * 随机的网络图片 -- 用于测试
     */
    public static final int[] randomNetPicsWidth = new int[]{800//800*599
            ,1280//800*599
            ,800//800*640
            ,800//800*599
            ,970//970*644
            ,1024//1024*768
            ,1024//1024*768
            ,1680//1680*2520
            ,1024//1024*740
            ,500//500*312
            ,1000//1000*666
            ,1920//1920*1080
            ,1024//1024*683
            ,678//678*1024
            ,778//778*1201
            ,1000//1000*1432
    };
    /**
     * 随机的网络图片 -- 用于测试
     */
    public static final int[] randomNetPicsHeight = new int[]{599//800*599
            ,852//800*599
            ,640//800*640
            ,599//800*599
            ,644//970*644
            ,768//1024*768
            ,768//1024*768
            ,2520//1680*2520
            ,740//1024*740
            ,312//500*312
            ,666//1000*666
            ,1080//1920*1080
            ,683//1024*683
            ,1024//678*1024
            ,1201//778*1201
            ,1432//1000*1432
    };
    /**
     * 随机的评论内容 -- 用于测试
     */
    public static final String[] randomContent = new String[]{"那么从身体上看呢，先是人的出生，从一个混沌的世界分离出来，总有一个独立的身体，这就是“分”了。一个婴儿需要来自各方的精心照顾才能长大，他需要在保持自身独立的先题条件下，在各方面融入他所身处的环境，又需要把他遇到的各种问题从大环境中单拿出来解决，这样反反复复，分分合合的走到了他生命的尽头——再次与大环境融合，重归混沌世界，重回大自然的怀抱，这就是“合”了"
            ,"但是真的什么都能合起来看吗?当然不能"
            ,"不是环境的原因，而是他自己的原因，而且不是什么事都要急着去做的，所以这个时候他就得分开看了，否则他和他身边的人都会生活的很痛苦"
            ,"而我们每时每刻都在不断的进行分与合的过程，只不过很多时候我们不自觉而已，如果我们只用分来处理问题，那样就会成为"
            ,"如果你不懂如果来分辨分与合的话，那么就先想想你的出生吧，“分”是为了生，“合”是为了活，只有生，才能活下去，如果没有生命力的东西，哪来的活下去呢?"
            ,"才能生活的滋润、惬意、自在。"
            ,"“古往今来，纵观史册，不外乎分分合合，分了又合，合了又分。”"
            ,"先从人的精神世界来说，我们是怎样看待我们所处的生活环境的?当然是合起来看，总不能说，美国就是美国，中国就是中国，它们之间没有什么关系吧?"
            ,"但是真的什么都能合起来看吗?当然不能"
            ,"比如一个急性子的人，他看谁都觉得慢，这当然不是环境的原因，而是他自己的原因，而且不是什么事都要急着去做的"
            ,"所以这个时候他就得分开看了，否则他和他身边的人都会生活的很痛苦。"
            ,"那么从身体上看呢，"
            ,"先是人的出生，从一个混沌的世界分离出来，总有一个独立的身体，这就是“分”了。一个婴儿需要"
            ,"来自各方的精心照顾才能长大，他需要在保持自身独立的先题条件下，在各方面融入他所身处的环境，又需要把他遇到的各种问题从大环境中单拿出来解决，这样反反复复，分分合合的走到了他生命的尽头——再次与大环境融合，重归混沌世界，重回大自然的怀抱，这就是“合”了。"
            ,"而我们每时每刻都在不断的进行分与合的过程，只不过很多时候我们不自觉而已，如果我们只用分来处理问题，那样就会成为人们口中的“愚昧”、“"
            ,"自私”、“棱角太多”的人，如果我们只用合来处理问题，也许又会成为“和稀泥”、“没主见”的人，所以，在保证自身特点的前提下，分分合合的处理问题，才能生活的滋润、惬意、自在。"
    };
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
     * 获取随机的网络图片
     * @return
     */
    public static String getRandomNetPics(){
        int index = (int) (Math.random() * randomNetPics.length);
        String random = RandomUtil.randomNetPics[index];
        return random;
    }
    /**
     * 获取随机的评论内容
     * @return
     */
    public static String getRandomContents(){
        int index = (int) (Math.random() * randomContent.length);
        String random = RandomUtil.randomContent[index];
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
