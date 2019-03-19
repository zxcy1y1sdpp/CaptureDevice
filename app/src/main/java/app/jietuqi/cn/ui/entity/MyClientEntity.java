package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

import app.jietuqi.cn.entity.OverallUserInfoEntity;

/**
 * 作者： liuyuanbo on 2019/3/15 10:18.
 * 时间： 2019/3/15 10:18
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class MyClientEntity implements Serializable {
    private static final long serialVersionUID = 1829769577012692026L;

    public AgencyInfoEntity superior;
    public ArrayList<OverallUserInfoEntity> z_user;
    public ArrayList<OverallUserInfoEntity> z_agent;
    public ArrayList<OverallUserInfoEntity> j_user;
    public ArrayList<OverallUserInfoEntity> j_agent;
}
