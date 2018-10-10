package com.ccj.client.android.analytics;

/**
 * Created by chenchangjun on 18/2/8.
 */

 class EConstant {

    static volatile boolean SWITCH_OFF = false; //全局开关,用于在接口返回时,控制sdk是否启动
    static volatile boolean DEVELOP_MODE = true; //全局开关,开发模式切换


    public static final String TAG = "JJEvent-->";



    /**
     * 数据库名称
     */
    static final String DB_NAME = "jjevent.db";
    static final int DB_VERSION = 1;//修改时,必须递增 ,


    /**
     * 接口地址
     */
    static    String COLLECT_URL = ""; //TODO 这里是要上传数据的接口


    /***********===================**time schedule**=============*********/

    /**
     * 记录到达xx条,主动进行上传,默认100
     */
    static int  PUSH_CUT_NUMBER = 100;

    /**
     * 上传间隔事件 分钟, 默认1分钟
     */
    static double PUSH_CUT_DATE = 1;

    /**
     * sid改变周期的标志:默认 15分钟
     */
    static int PUSH_FINISH_DATE = 1;



    /**
     * 统计类别
     */
    public static final String EVENT_TYPE_DEFAULT = "default";//暂时先用默认值
    public static final String EVENT_TYPE_PV = "screenview";//屏幕
    public static final String EVENT_TYPE_EVENT = "event";//点击
    public static final String EVENT_TYPE_EXPOSE = "show";//曝光,接口不同






}
