package com.ccj.client.android.analytics;

import android.text.TextUtils;

import com.ccj.client.android.analytics.bean.EventBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class ExposeTask implements Runnable {


    private String exposeID;
    private String ec;
    private String ea;
    private Map mapEcp;


    public ExposeTask(String exposeID, String ec, String ea, Map mapEcp) {
        this.exposeID = exposeID;
        this.ec = ec;
        this.ea = ea;
        this.mapEcp = mapEcp;
    }

    @Override
    public void run() {
        if (!JJEventManager.hasInit) {
            ELogger.logError(EConstant.TAG, "please init JJEventManager!");
            return;
        }

        if (EConstant.SWITCH_OFF) {
            ELogger.logWrite(EConstant.TAG, "the sdk is SWITCH_OFF");
            return;
        }


        try {
            //ELogger.logWrite(EConstant.TAG, "thread-"+Thread.currentThread().getName());


            EventBean bean = EventDecorator.generateExposedBean(exposeID, ec, ea, mapEcp);


            if (bean == null) {
                ELogger.logWrite(EConstant.TAG, "expose bean == null");
                return;
            }


            ELogger.logWrite(EConstant.TAG, "thread-"+Thread.currentThread().getName()+",expose " + bean.toString());


            if (!TextUtils.isEmpty(bean.getExposed_id())) {//如果数据中有exposed id则执行 查看操作
                List<EventBean> resultList = EDBHelper.getEventByExposedID(bean.getExposed_id());

                if (resultList != null && resultList.size() > 0) {//如果存在id,则更新
                    ELogger.logError(EConstant.TAG, "Exposed_id-"+bean.toString()+"exposed id 有重复数据 ");

                    EDBHelper.updateEventBean(bean, bean.getExposed_id());
                } else {
                    EDBHelper.addEventData(bean);//不存在id,则新增
                    EventDecorator.pushEventByNum();
                }
            } else {//如果没有exposedid 则正常添加
                EDBHelper.addEventData(bean);
                EventDecorator.pushEventByNum();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    @Override
    public String toString() {
        return "ExposeTask{" +
                "exposeID='" + exposeID + '\'' +
                ", ec='" + ec + '\'' +
                ", ea='" + ea + '\'' +
                ", mapEcp=" + mapEcp +
                '}';
    }
}
