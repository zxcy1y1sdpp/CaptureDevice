package app.jietuqi.cn.wechat.widget.groupicon.region;

import android.graphics.Region;

public interface IRegionManager {
    Region[] calculateRegion(int size, int subSize, int gap, int count);
}
