package app.jietuqi.cn.http;

/**
 * 作者： liuyuanbo on 2018/11/7 11:07.
 * 时间： 2018/11/7 11:07
 * 邮箱： 972383753@qq.com
 * 用途： 网络配置相关，包括baseurl，接口地址，超市时间等
 */

public class HttpConfig {
    /** 超时时间，链接时间*/
    public static int HTTP_FAIL_TIME = 30000;
    /**
     * 网络请求的总路径
     */
    public static String BASE_URL="http://www.jietuqi.cn";
    /**
     * AppId
     **/
    public static String APPID = "6055172231";
    /**
     * AppSecret
     **/
    public static String APPSECRET = "fCholNwTgdrGLXLzWAZMPDKOvNHPEgSA";

    /************************************ 接口地址 ************************************/
    /**
     * 1: 登录和注册通用的接口地址
     */
    public final static String REGISTER_AND_LOGIN = "/api/users";
    /**
     * 2: 零散的接口地址
     */
    public final static String INDEX = "/api/index";
    /**
     * 3: 创建订单的接口
     */
    public final static String ORDER = "/api/order";
    /**
     * 4: 上传图片的接口
     */
    public final static String UPLOAD = "/api/upload";
    /**
     * 5: 上传图片的动态的接口（不包含图片，只包含图片id的拼接）
     */
    public final static String INFO = "/api/info";
    /**
     * 6: 修改用户信息的接口
     */
    public final static String USERS = "/api/users";
    /**
     * 7: 获取行业类别的接口
     */
    public final static String INFORMATION = "/api/information";
    public final static String STORE = "/api/store";
    public final static String GOLD = "/api/gold";
    public final static String GOODS = "/api/goods";
}
