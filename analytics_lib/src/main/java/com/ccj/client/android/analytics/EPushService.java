package com.ccj.client.android.analytics;

import java.util.Timer;
import java.util.TimerTask;

import static com.ccj.client.android.analytics.EConstant.SWITCH_OFF;

/**
 *  *  * 后台服务,app启动时开启.
 * Created by chenchangjun on 18/2/24.
 */

 class EPushService  {

    private static EPushService pushService;

    /**
     * Timer不保证任务执行的十分精确。
     * Timer类的线程安全的。
     */
    private  Timer timer = new Timer();
    private TimerTask task;
    private static boolean timerHasCanceled=false;


    private void init() {
        ELogger.logWrite(EConstant.TAG, " TimerTask init  on thread-->"+Thread.currentThread().getName());
        task = new TimerTask() {
            @Override
            public void run() {
                //经过验证 run  on thread-->Timer-0,并不在主线程,省去了我的工作...
                if (SWITCH_OFF) {
                    return;
                }
                EPushTask.pushEvent();
            }
        };
        if (timerHasCanceled){
            timer=new Timer();
            timerHasCanceled=false;
        }
        double periodD=  (EConstant.PUSH_CUT_DATE * 60 * 1000);
        int period = Double.valueOf(periodD).intValue();
        timer.schedule(task, 60 * 1000,period );//延时1分钟后执行,每次执行间隔PUSH_CUT_DATE分钟
    }


    /**
     * 主动调用push操作,运行在主线程中.
     */
    public void excutePushEvent() {
        if (SWITCH_OFF) {
            ELogger.logWrite(EConstant.TAG, " excutePushEvent  is SWITCH_OFF,please check SWITCH_OFF is true or false!");
            return;
        }
        EPushTask.pushEvent();
    }


    /**
     * 停止 推送 服务
     */
    public void stopEventService() {
        ELogger.logWrite(EConstant.TAG, " EPushService is stop");
        if (task != null && timer != null) {
            timer.cancel();
            task.cancel();
            timerHasCanceled=true;

        }

    }





    /**************generate*****************/


    /**
     * 启动 定时推送服务
     */
    public static void startService() {
        if (pushService!=null){
            getSingleInstance().init();
        }else {
            getSingleInstance();
        }

    }

    /**
     * 获取 推送服务 单例
     * @return
     */
    public static EPushService getSingleInstance() {

        if (pushService == null) {
            synchronized (JJEventManager.class) {//双重检查
                if (pushService == null) {
                    pushService = new EPushService();

                }
            }
        }
        return pushService;
    }


    private EPushService() {
        init();
    }


}
