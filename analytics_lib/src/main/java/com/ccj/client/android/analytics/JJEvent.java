package com.ccj.client.android.analytics;

import com.ccj.client.android.analytics.enums.LTPType;
import com.ccj.client.android.analytics.thread.JJPoolExecutor;

import java.util.Map;
import java.util.concurrent.FutureTask;


/**
 * 统计入口
 * Created by chenchangjun on 18/2/8.
 */
public final class JJEvent {


    /**
     * pageview 屏幕值
     *
     * @param sn  screen 屏幕值,例`Android/主页/好物`
     * @param ltp 屏幕加载方式
     */
    public static void screen(String sn, LTPType ltp) {
        screen(sn, ltp, null);
    }


    /**
     * 点击事件
     *
     * @param ec event category 事件类别
     * @param ea event action 事件操作
     * @param el event label 事件标签
     */
    public static void event(String ec, String ea, String el) {
        event(ec, ea, el, null);
    }


    /**
     * pageview 屏幕值
     *
     * @param sn  screen 屏幕值,例`Android/主页/好物`
     * @param ltp 屏幕加载方式
     * @param ecp event custom Parameters 自定义参数Map<key,value>
     */

    public static void screen(String sn, LTPType ltp, Map ecp) {

        try {
            ScreenTask screenTask =new ScreenTask(sn,ltp,ecp);
            JJPoolExecutor.getInstance().execute(new FutureTask<Object>(screenTask,null));
        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "expose " + e.getMessage());

        }


    }


    /**
     * 点击事件
     *
     * @param ec  event category 事件类别
     * @param ea  event action 事件操作
     * @param el  event label 事件标签
     * @param ecp event custom Parameters 自定义参数Map<key,value>
     */
    public static void event(String ec, String ea, String el, Map ecp) {

        try {
            EventTask eventTask =new EventTask(ec,ea,el,ecp);
            JJPoolExecutor.getInstance().execute(new FutureTask<Object>(eventTask,null));
        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "event error " + e.getMessage());

        }





    }




    /**
     * 曝光
     * @param exposeID 去重用
     * @param ec  event category 事件类别
     * @param ea  event action 事件操作
     * @param ecp event custom Parameters 自定义参数Map<key,value>
     */
    public static synchronized void expose(String exposeID, String ec, String ea, Map ecp) {

        // expose(exposeID, ec, ea, ecp, null);

        try {
            ExposeTask exposeTask =new ExposeTask(exposeID,ec,ea,ecp);

            JJPoolExecutor.getInstance().execute(new FutureTask<Object>(exposeTask,null));
        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "expose error " + e.getMessage());

        }


    }


}
