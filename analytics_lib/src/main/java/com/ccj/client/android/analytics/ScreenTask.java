package com.ccj.client.android.analytics;

import com.ccj.client.android.analytics.bean.EventBean;
import com.ccj.client.android.analytics.enums.LTPType;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class ScreenTask implements Runnable {


    private String sn;
    private LTPType ltp;
    private Map ecp;


    public ScreenTask(String sn, LTPType ltp, Map ecp) {
        this.sn = sn;
        this.ltp = ltp;
        this.ecp = ecp;
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
            EventBean bean = EventDecorator.generateScreenBean(sn, ltp, ecp);


            if (bean == null) {
                ELogger.logWrite(EConstant.TAG, "thread-"+Thread.currentThread().getName()+", screen bean == null");
                return;
            }
            ELogger.logWrite(EConstant.TAG, "screen " + bean.toString());

            EDBHelper.addEventData(bean);



            EventDecorator.pushEventByNum();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        return "ScreenTask{" +
                "sn='" + sn + '\'' +
                ", ltp=" + ltp +
                ", ecp=" + ecp +
                '}';
    }
}
