package com.ccj.client.android.analytics;

import android.text.TextUtils;

import com.ccj.client.android.analyticlib.BuildConfig;
import com.ccj.client.android.analytics.bean.EventBean;
import com.ccj.client.android.analytics.enums.LTPType;
import com.ccj.client.android.analytics.net.gson.EGson;
import com.ccj.client.android.analytics.net.gson.GsonBuilder;
import com.ccj.client.android.analytics.utils.EMD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事件修饰类,提供event参数
 * Created by chenchangjun on 18/2/8.
 */

class EventDecorator {


    private static final String TAG = EConstant.TAG;
    private static volatile String old_date = "2010-01-01 00:00:00";//生成一次后缓存在此,初始化为时间.
    private static volatile String sid = "";//生成一次后缓存在此
    private static String cookie = "";//从宿主app中获取cookie

    private static final AtomicInteger eventNum = new AtomicInteger(0);//当满足连续操作大于100条,就进行上传服务

   // private static  volatile long hitsCount = 0;//当前页面在一次访问中的第几次数据请求；与session_id关联，当session_id变化时重新计数，从1开始

    private static  final AtomicInteger hitsCount=new AtomicInteger(0) ;//当前页面在一次访问中的第几次数据请求；与session_id关联，当session_id变化时重新计数，从1开始

    //MD5摘要，用于校验md5(dt+cid+type+salt)
    private static String salt="d41d8cd98f00b204e9800998ecf84";


    public static synchronized void initCookie(String cookieStr) {
        cookie = cookieStr;
        //添加sdk版本
        cookie += "sv=" + getURLEncode(BuildConfig.VERSION_NAME) + ";";
        cookie += "st=" + getURLEncode("android") + ";";
        ELogger.logWrite(TAG, "initCookie successful--> " + cookie);
    }


    public static synchronized EventBean generateEventBean(String ec, String ea, String el, Map ecp) {


        EventBean bean = generateCommonBean(ecp);
        //event
        if (ec != null && !ec.isEmpty()) {
            bean.setEc(ec);
        }

        if (ea != null && !ea.isEmpty()) {
            bean.setEa(ea);
        }

        if (el != null && !el.isEmpty()) {
            bean.setEl(el);
        }
        bean.setType(EConstant.EVENT_TYPE_EVENT);

        return bean;
    }


    public static synchronized EventBean generateScreenBean(String sn, LTPType ltp, Map ecp) {

        EventBean bean = generateCommonBean(ecp);

        //screen

        if (sn != null && !sn.isEmpty()) {
            bean.setSn(sn);
        }
        if (ltp != null) {
            bean.setLtp(ltp.getTypeName());
        }
        bean.setType(EConstant.EVENT_TYPE_PV);

        return bean;
    }

    /**
     * 把 修改全局静态变量, 都放在这里处理,用synchronized修饰, 保证线程安全.
     * @param ecp
     * @return
     */
    private static synchronized EventBean generateCommonBean(Map ecp) {


        EventBean bean = new EventBean();
        //common
        bean.setV(BuildConfig.VERSION_NAME);
        bean.setIt(EventDecorator.getIT());
        bean.setTid(EventDecorator.getTID());
        bean.setSid(EventDecorator.getSID());
        bean.setHnb(EventDecorator.getHnbCount());
        bean.setDs("app");
        //自定义
        if (ecp != null && !ecp.isEmpty()) {

            EGson EGson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String ecpStr = EGson.toJson(ecp);
            bean.setEcp(ecpStr);
        }

        EventDecorator.refreshCurrentEventDate();//刷新点击 时间,用于比较下次点击事件,计算Sid

        return bean;
    }




    public static  synchronized  EventBean generateExposedBean(String exposeID, String ec, String ea, Map mapEcp) {



        EventBean bean = generateCommonBean(mapEcp);

        //expsed
        if (ec != null && !ec.isEmpty()) {
            bean.setEc(ec);
        }

        if (ea != null && !ea.isEmpty()) {
            bean.setEa(ea);
        }



        if (!TextUtils.isEmpty(exposeID)){
            bean.setExposed_id(exposeID);

        }else {
            bean.setExposed_id("");

        }


        bean.setM(EMD5Utils.MD5(bean.getEa()+"?"+bean.getIt()+salt));
        bean.setType(EConstant.EVENT_TYPE_EXPOSE);
        return bean;
    }

    static synchronized void pushEventByNum() {
        EventDecorator.addEventNum();
        if (EventDecorator.getEventNum() >= EConstant.PUSH_CUT_NUMBER) { //当满足连续操作大于100条,就进行上传服务
            //  JJEventService.pushEvent();
            EPushService.getSingleInstance().excutePushEvent();
            EventDecorator.clearEventNum();
            ELogger.logWrite(EConstant.TAG, "当满足连续操作大于" + EConstant.PUSH_CUT_NUMBER + "条,就进行上传服务");

        }
    }


    public static String getTID() {
        //TODO
        return "UA-1000000-2";
    }

    /**
     * 访问结束的标志:不活动状态超过15分钟；由客户端生成
     */
    public static synchronized   String getSID() {
        //
        String newDate = getNowDate();

        if (EventDecorator.compareDate(newDate, old_date, EConstant.PUSH_FINISH_DATE)) {
            sid = getNewUniqueSid() + "";
            hitsCount.set(0); //ssid变化时,重新计数
        }

        return sid;
    }


    public static synchronized   String getNowDate() {
        //    SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于now.toLocaleString()
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = format.format(date);
        return strDate;
    }

    /**
     * 日志时间 时间戳
     *
     * @return
     */
    public static synchronized String getIT() {
        //    SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于now.toLocaleString()
        return String.valueOf(System.currentTimeMillis());
    }

    private static  synchronized String getNewUniqueSid() {
        int radom = (int) (Math.random() * 9000 + 1000);//四位随机数
        return System.currentTimeMillis() + "" + radom;
    }


    public static synchronized  String  getHnbCount() {
        return hitsCount.incrementAndGet()+"";
    }


    public static  void refreshCurrentEventDate() {
        old_date = getNowDate();
    }

    public static String getRequestCookies() {
        if (cookie.isEmpty()) {
            ELogger.logError(TAG, "cookie is empty ");
        }
        return cookie;
    }


    public static  int getEventNum() {
        return eventNum.get();
    }

    public static  void addEventNum() {
        eventNum.incrementAndGet();
    }

    public static  void clearEventNum() {
        eventNum .set(0);
    }


    /**
     * 访问结束的标志:不活动状态超过15分钟
     */
    private static synchronized boolean compareDate(String newDate, String oldDate, int minute) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dtNew = null;
        Date dtOld = null;

        try {

            dtNew = format.parse(newDate);
            dtOld = format.parse(oldDate);

            long offset = dtNew.getTime() - dtOld.getTime();
            long standard = 60 * 1000 * minute;

            if (offset > standard) {//不活动状态超过15分钟
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(TAG, e.getMessage());
            return true;

        }
    }

    public static String getURLEncode(String value) {
        String result = "";

        try {
            result = URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }


}
