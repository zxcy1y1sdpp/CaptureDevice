package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者： liuyuanbo on 2019/3/15 17:13.
 * 时间： 2019/3/15 17:13
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class AgencyIncomeAndExpensesEntity implements Serializable {
    private static final long serialVersionUID = 8134638095474051379L;
    /**
     * 共有多少条数据
     */
    public int total;
    /**
     * 当前页有多少条数据
     */
    public int per_page;
    /**
     * 当前是第几页
     */
    public int current_page;
    /**
     * 一共有多少页数据
     */
    public int last_page;
    public ArrayList<Data> data;
    public class Data implements Serializable{
        private static final long serialVersionUID = -6107787504728572050L;
        public int id;
        public int users_id;
        public String alipay_name;
        public String alipay_account;
        public String money;
        public Type type;
        public int status;
        public String create_time;
        /**
         * 一共有多少条数据
         */
        public int total;
    }
    public class Type implements Serializable{
        private static final long serialVersionUID = -5164217340448139035L;
        public String text;
        public int value;
    }
}
