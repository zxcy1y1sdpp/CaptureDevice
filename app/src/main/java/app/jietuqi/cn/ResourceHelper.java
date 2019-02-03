package app.jietuqi.cn;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： liuyuanbo on 2019/1/26 16:51.
 * 时间： 2019/1/26 16:51
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class ResourceHelper {
    private static Map<String, Integer> appIconIdMap = new HashMap<>();
    static {
        // 本地头像
        appIconIdMap.put("R.mipmap.icon0", R.mipmap.icon0);
        appIconIdMap.put("R.mipmap.icon2", R.mipmap.icon2);
        appIconIdMap.put("R.mipmap.icon1", R.mipmap.icon1);
        appIconIdMap.put("R.mipmap.icon3", R.mipmap.icon3);
        appIconIdMap.put("R.mipmap.icon4", R.mipmap.icon4);
        appIconIdMap.put("R.mipmap.role_002", R.mipmap.role_002);
        appIconIdMap.put("R.mipmap.role_003", R.mipmap.role_003);
        appIconIdMap.put("R.mipmap.role_004", R.mipmap.role_004);
        appIconIdMap.put("R.mipmap.role_005", R.mipmap.role_005);
        appIconIdMap.put("R.mipmap.role_006", R.mipmap.role_006);
        appIconIdMap.put("R.mipmap.role_007", R.mipmap.role_007);
        appIconIdMap.put("R.mipmap.role_008", R.mipmap.role_008);
        appIconIdMap.put("R.mipmap.role_009", R.mipmap.role_009);
        appIconIdMap.put("R.mipmap.role_022", R.mipmap.role_022);
        appIconIdMap.put("R.mipmap.role_011", R.mipmap.role_011);
        appIconIdMap.put("R.mipmap.role_012", R.mipmap.role_012);
        appIconIdMap.put("R.mipmap.role_013", R.mipmap.role_013);
        appIconIdMap.put("R.mipmap.role_014", R.mipmap.role_014);
        appIconIdMap.put("R.mipmap.role_015", R.mipmap.role_015);
        appIconIdMap.put("R.mipmap.role_016", R.mipmap.role_016);
        appIconIdMap.put("R.mipmap.role_017", R.mipmap.role_017);
        appIconIdMap.put("R.mipmap.role_018", R.mipmap.role_018);
        appIconIdMap.put("R.mipmap.role_020", R.mipmap.role_020);
        appIconIdMap.put("R.mipmap.role_019", R.mipmap.role_019);
        appIconIdMap.put("R.mipmap.role_021", R.mipmap.role_021);
        appIconIdMap.put("R.drawable.wechat_nongyeyinhang", R.drawable.wechat_nongyeyinhang);
        appIconIdMap.put("R.drawable.wechat_zhongguoyinhang", R.drawable.wechat_zhongguoyinhang);
        appIconIdMap.put("R.drawable.wechat_jiansheyinhang", R.drawable.wechat_jiansheyinhang);
        appIconIdMap.put("R.drawable.wechat_zhaoshangyinhang", R.drawable.wechat_zhaoshangyinhang);
        appIconIdMap.put("R.drawable.wechat_minshengyinhang", R.drawable.wechat_minshengyinhang);
        appIconIdMap.put("R.drawable.wechat_jiaotongyinhang", R.drawable.wechat_jiaotongyinhang);
        appIconIdMap.put("R.drawable.wechat_huaxiayinhang", R.drawable.wechat_huaxiayinhang);
        appIconIdMap.put("R.drawable.wechat_gongshangyinhang", R.drawable.wechat_gongshangyinhang);
        appIconIdMap.put("R.drawable.wechat_youzhengyinhang", R.drawable.wechat_youzhengyinhang);
    }
    /**
     * 我的应用
     * 根据appName动态获取app的iconId
     * @param appName
     * @return
     */
    public static Integer getAppIconId(String appName){
        if (appIconIdMap.containsKey(appName)){
            return appIconIdMap.get(appName);
        }
        return 0;
    }
}
