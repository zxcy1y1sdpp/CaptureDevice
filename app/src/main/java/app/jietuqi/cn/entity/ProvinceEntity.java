package app.jietuqi.cn.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 刘远博 on 2017/1/5.
 */

public class ProvinceEntity implements Serializable{
    /**
     * 接口请求状态 0 -- 成功
     */
    public String status;
    /**
     * 需要用到的实体数据
     */
    public Procince data;
    public static class Procince implements Serializable{
        public List<City> list;
    }

    public static class City implements Serializable{
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
