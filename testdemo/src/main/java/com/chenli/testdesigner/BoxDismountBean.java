package com.chenli.testdesigner;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/2.
 */

public class BoxDismountBean implements Serializable{
    public int id;

    public String userid;
    /**
     * 集装箱
     */
    public String cntrnum;
    /**
     * 是否拆空
     */
    public int passcntr;
    /**
     * 是否放行
     */
    public boolean ispass;
    /**
     * 是否使用
     */
    public int cntrtype;

    public String creattime;

    @Override
    public String toString() {
        return "BoxDismountBean{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", cntrnum='" + cntrnum + '\'' +
                ", passcntr=" + passcntr +
                ", ispass=" + ispass +
                ", cntrtype=" + cntrtype +
                ", createtime=" + creattime +
                '}';
    }
}
