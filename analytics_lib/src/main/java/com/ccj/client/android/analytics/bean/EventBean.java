package com.ccj.client.android.analytics.bean;


import com.ccj.client.android.analytics.db.annotations.Table;
import com.ccj.client.android.analytics.db.annotations.Transient;

import java.io.Serializable;

/**
 * Created by chenchangjun on 18/2/8.
 */

@Table(name = "eventlist")
public class EventBean implements Serializable{

    @Transient
    private static final long serialVersionUID = 9009411034336334765L;




    private int id;

    private String type;//统计类别,  EVENT,PV 等等
    private String  ds;//表示匹配的数据来源。用来区分app与H5

    private String it;//日志事件
    private String sid;//访问结束的标志:不活动状态超过15分钟；由客户端生成
    private String hnb; //当前页面在一次访问中的第几次数据请求；与session_id关联，当session_id变化时重新计数，从1开始
    private String  v; //当前值为“1”。只有出现不向后兼容的更改时，此值才会改变。
    private String  tid;//格式为 UA-XXXX-Y。所有收集的数据都与此 ID 相关联。
    private String  sn;//屏幕名称
    private int  ltp;//屏幕加载方式
    private String  ec;//事件类别
    private String  ea;//事件操作
    private String  el;//事件标签

    private String ecp;//自定义map 存储

    private String m;//MD5摘要，用于校验md5_digest"salt：d41d8cd98f00b204e9800998ecf8427e md5(dt+cid+type+salt)"
    private String exposed_id;//曝光id

    private String sp;//showtype曝光位置 0:feed流，1:banner位



/*
*
*   "links": [
        {
            "name": "Google",
            "url": "http://www.google.com"
        },
        {
            "name": "Baidu",
            "url": "http://www.baidu.com"
        },
        {
            "name": "SoSo",
            "url": "http://www.SoSo.com"
        }
    ]
* */

    @Override
    public String toString() {
        return "EventBean{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", ds='" + ds + '\'' +
                ", it='" + it + '\'' +
                ", sid='" + sid + '\'' +
                ", hnb='" + hnb + '\'' +
                ", v='" + v + '\'' +
                ", tid='" + tid + '\'' +
                ", sn='" + sn + '\'' +
                ", ltp=" + ltp +
                ", ec='" + ec + '\'' +
                ", ea='" + ea + '\'' +
                ", el='" + el + '\'' +
                ", ecp='" + ecp + '\'' +
                ", m='" + m + '\'' +
                ", exposed_id='" + exposed_id + '\'' +
                ", sp='" + sp + '\'' +
                '}';
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getExposed_id() {
        return exposed_id;
    }

    public void setExposed_id(String exposed_id) {
        this.exposed_id = exposed_id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getHnb() {
        return hnb;
    }

    public void setHnb(String hnb) {
        this.hnb = hnb;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int  getLtp() {
        return ltp;
    }

    public void setLtp(int  ltp) {
        this.ltp = ltp;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getEa() {
        return ea;
    }

    public void setEa(String ea) {
        this.ea = ea;
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public String getEcp() {
        return ecp;
    }

    public void setEcp(String ecp) {
        this.ecp = ecp;
    }




/*

private String sv;
private String st;
private String dm;
private String dt;
private String os;
private String ca;
private String nt;
private String ch;
private String lt;
private String ip;
private String did;
private String sid;
private String hnb;
private String imei;
private String mac;
private String uuid;
private String idfa;
private String idfv;
private String v;
private String tid;
private String ds;
private String uid;
private String sr;
private String t;
private String an;
private String aid;
private String av;
private String sn;
private String ltp;
private String ec;
private String ea;
private String el;
private String ift;*/
}
