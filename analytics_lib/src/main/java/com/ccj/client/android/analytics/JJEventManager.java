package com.ccj.client.android.analytics;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.ccj.client.android.analytics.exception.EventException;
import com.ccj.client.android.analytics.intercept.CookieFacade;
import com.ccj.client.android.analytics.utils.EDeviceUtils;

import static com.ccj.client.android.analytics.EConstant.TAG;


/**
 * 事件管理
 * Created by chenchangjun on 18/2/8.
 */

public final class JJEventManager {

    public static boolean IS_DEBUG = EConstant.DEVELOP_MODE;

    private static Application app;//全局持有app,保证sdk正常运转. app引用与进程同生命周期, 即 进程被销毁, jvm会随之销毁,app引用会随之销毁. so不存在内存泄漏.
    protected volatile static boolean hasInit = false;

    /**
     * 获取application 上下文
     *
     * @return
     */
    public static Context getContext() {
        if (app == null) {
            throw new EventException("请先在application中实例化JJEventManager");
        }
        return app;
    }


    /**
     * 初始化sdk, 要在application中的onCreate() 方法中进行初始化.
     *
     * @param application 全局上下文
     * @param cookie      宿主app中的通用cookie
     * @param isDebug     是否是debug模式(控制开启log等)
     */
    public static void init(Application application, String cookie,boolean isDebug) {

            if (application==null){
                ELogger.logWrite(EConstant.TAG, " JJEventManager application==null!");
                return;
            }

            //处理app拥有多个进程
            String processName = EDeviceUtils.getProcessName(application, Process.myPid());
            if (processName==null||!processName.equals(application.getPackageName()+"")) {
                ELogger.logWrite(EConstant.TAG, " JJEventManager 初始化进程为:" + processName + ",不在主进程中!");
                return;
            }


            if (hasInit) {
                ELogger.logWrite(EConstant.TAG, " JJEventManager 已经初始化init(),请勿重复操作!!!!!!");
                // throw new EventException("JJEventManager 已经初始化init()");
                return;
            }


            hasInit = true;
            EConstant.SWITCH_OFF = false;//开启一切统计事务
            EConstant.DEVELOP_MODE = isDebug;//是否是开发模式


            /****************进行初始化*************************/
            app = application;
            EPushService.startService();

            EventDecorator.initCookie(cookie);

            ELogger.logWrite(EConstant.TAG, " JJEventManager run  on thread-->" + Thread.currentThread().getName());
            ELogger.logWrite(TAG, "----JJEvent sdk init  success!----");


    }


    public static void pushEvent() {
        EPushService.getSingleInstance().excutePushEvent();

    }


    /**
     * 用于刷新Cookie
     *
     * @param cookie
     */
    public static void refreshCookie(String cookie) {
        EventDecorator.initCookie(cookie);

    }

    /**
     * 停止sdk所有服务(停止事件统计,停止事件推送)
     */
    public static void destoryEventService() {
        hasInit = false;//变为 可初始化
        EConstant.SWITCH_OFF = true;//关闭一切统计事务
        EPushService.getSingleInstance().stopEventService();
        ELogger.logWrite(EConstant.TAG, " ----JJEvent sdk is destoryEventService!---");

    }


    /**
     * 停止事件的上传任务(仍会记录事件,停止事件推送)
     */
    public static void cancelEventPush() {
        hasInit = false;//变为 可初始化
        EPushService.getSingleInstance().stopEventService();
        ELogger.logWrite(EConstant.TAG, " ----JJEvent sdk is cancelEventPush---");

    }

    /**
     * 内部构建类
     * 优势:可以根据需求,在不改变原有架构API的基础上,灰常灵活的进行构建修改,方便的很~
     */
    public static class Builder {

        private Application application;

        private boolean DEVELOP_MODE = EConstant.DEVELOP_MODE;

        private int PUSH_CUT_NUMBER = EConstant.PUSH_CUT_NUMBER;
        private double PUSH_CUT_DATE = EConstant.PUSH_CUT_DATE;
        private int PUSH_FINISH_DATE = EConstant.PUSH_FINISH_DATE;

        private String cookie = "";
        private CookieFacade cookieIntercept;


        public Builder(Application application) {
            this.application = application;

        }

        /**
         * 宿主 cookie
         * @param cookie
         * @return
         */
        public Builder setHostCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }


        /**
         * 是否是开发者模式
         * @param isDebug
         * @return
         */
        public Builder setDebug(boolean isDebug) {
            DEVELOP_MODE = isDebug;
            return this;
        }

        /**
         * 主动推送上限数
         * @param num
         * @return
         */
        public Builder setPushLimitNum(int num) {
            PUSH_CUT_NUMBER = num;
            return this;
        }

        /**
         * 推送周期
         * @param minutes
         * @return
         */
        public Builder setPushLimitMinutes(double minutes) {
            PUSH_CUT_DATE = minutes;
            return this;
        }

        /**
         * sid 改变周期
         * @param minutes
         * @return
         */
        public Builder setSidPeriodMinutes(int minutes) {
            PUSH_FINISH_DATE = minutes;
            return this;
        }

        /**
         * 设置服务器的请求接口
         * @param url
         * @return
         */
        public  Builder setPushUrl(String   url) {
            EConstant. COLLECT_URL = url;
            return this;
        }
        /**
         * cookie 动态注入接口
         * @param cookieIntercept
         * @return
         */
        public Builder setCookieIntercept(CookieFacade cookieIntercept) {
            this.cookieIntercept=cookieIntercept;
            return this;
        }

        /**
         * 开始构建
         */
        public void start() {
            ELogger.logWrite(EConstant.TAG, " JJEventManager.Builder#start() " );

            if (application == null) {
                ELogger.logWrite(EConstant.TAG, " JJEventManager.Builder#start() application:" + "不能为空!");
                return;
            }

            //处理app拥有多个进程
            String processName = EDeviceUtils.getProcessName(application, Process.myPid());
            if (!processName.equals(application.getPackageName())) {
                ELogger.logWrite(EConstant.TAG, " JJEventManager.Builder#start() 初始化进程为:" + processName + " 不在主进程中!");
                return;
            }

            EConstant.PUSH_CUT_NUMBER = PUSH_CUT_NUMBER;
            EConstant.PUSH_CUT_DATE = PUSH_CUT_DATE;
            EConstant.PUSH_FINISH_DATE = PUSH_FINISH_DATE;
            EGsonRequest.cookieIntercept=cookieIntercept;

            JJEventManager.init(application, cookie, DEVELOP_MODE);
        }
    }


}
