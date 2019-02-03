package app.jietuqi.cn.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 刘远博 on 2017/1/5.
 */

public class ProvinceEntity implements Serializable{
    private static final long serialVersionUID = -963648902021866422L;
    /**
     * 接口请求状态 0 -- 成功
     */
    public String status;
    /**
     * 需要用到的实体数据
     */
    public Procince data;
    public static class Procince implements Serializable{
        private static final long serialVersionUID = -2878799215035489663L;
        public List<City> list;
    }

    public static class City implements Serializable{
        private static final long serialVersionUID = 5998075388662232427L;
        /**
         * 市辖区对应的编号
         */
        public String id;
        /**
         * 市辖区对应的名称
         */
        public String name;
        /**
         * 市辖区对应的名称
         */
        public List<AreaList> cityList;
    }

    public static class AreaList implements Serializable{
        private static final long serialVersionUID = -7830260330179374330L;
        /**
         * 市辖区对应的编号
         */
        public String id;
        /**
         * 市辖区对应的名称
         */
        public String name;
        /**
         * 市辖区对应的名称
         */
        public List<Area> areaList;
        public boolean isCheck;
    }

    public static class Area implements Serializable {
        private static final long serialVersionUID = 5043716659961647372L;
        /**
         * 市辖区对应的编号
         */
        public String id;
        /**
         * 市辖区对应的名称
         */
        public String name;
        public boolean isCheck;
    }
}
