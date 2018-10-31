package dasheng.com.capturedevice.util;

import java.util.List;

/**
 * 作者： liuyuanbo on 2018/10/17 23:18.
 * 时间： 2018/10/17 23:18
 * 邮箱： 972383753@qq.com
 * 用途： List相关的方法
 */

public class ListUtil {
    /**
     * 获取list中存放的最后一个元素
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getLastElement(List<T> list) {
        return list.get(list.size() - 1);

    }
}
