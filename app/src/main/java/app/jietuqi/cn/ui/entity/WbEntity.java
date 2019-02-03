package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/1/29 14:02.
 * 时间： 2019/1/29 14:02
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class WbEntity implements Serializable {
    private static final long serialVersionUID = -3404988386759968109L;
    public int id;
    public int status;
    public String title;
    public int time;
    public String price;
    public String type;
    public String classify;
    public int level;
    public int ratio;
    public String time_name;
    public String picture_url;
    public int gold;
    public String payChannel;
    public String orderNum;
    /*
    * "id": 8,
            "status": 1,
            "update_time": 1548732810,
            "create_time": 1548732810,
            "title": "100",
            "time": 0,
            "price": "10.00",
            "type": "",
            "classify": "gold",
            "level": 4,
            "ratio": 10,
            "time_name": "",
            "picture_url": "http://www.jietuqi.cn/uploads/picture/20190129/75df4d8f15466ae8693ac607bd9a5427.png",
            "gold": 100*/
}
