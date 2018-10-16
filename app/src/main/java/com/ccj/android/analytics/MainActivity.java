package com.ccj.android.analytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ccj.client.android.analytics.JJEvent;
import com.ccj.client.android.analytics.JJEventManager;
import com.ccj.client.android.analytics.intercept.CookieFacade;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "TAG";
    TextView tv_show;
    Button btn_event, btn_pv, btn_cancel, btn_start,btn_push;
    private int i = 0, j = 0;
    int haha=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_show = findViewById(R.id.tv_show);
        btn_event = findViewById(R.id.btn_event);
        btn_pv = findViewById(R.id.btn_pv);
        btn_push=findViewById(R.id.btn_push);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_start = findViewById(R.id.btn_start);


        /**
         * 多线程操作
         */
        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int k = 0; k < 40; k++) {
                    //添加自定义参数ecp,ecp默认为null
                    Map ecp = new HashMap();
                    ecp.put("自定义key1", "自定义value1");
                    ecp.put("自定义key2", "自定义value2");
                    JJEvent.event("event " + k, "event ea","event el");

                }
             /*   //添加自定义参数ecp,ecp默认为null
                Map ecp = new HashMap();
                ecp.put("自定义key1", "自定义value1");
                ecp.put("自定义key2", "自定义value2");
                JJEvent.expose("ss", "首页", "点击" + "button" + (++i), ecp);*/
            }
        });

        /**
         * 主动推送
         * */
        btn_pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加自定义参数ecp,ecp默认为null
                Map ecp = new HashMap();
                ecp.put("自定义key1", "自定义value1");
                ecp.put("自定义key2", "自定义value2");
                JJEvent.expose("ss1", "首页", "点击" + "button" + (++i), ecp);
                JJEventManager.pushEvent();
            }
        });


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //JJEventManager.init(getApplication(), "第二次cookie", "dt", "cid", true); //方式1


                JJEventManager.Builder builder = new JJEventManager.Builder(getApplication()); //方式2
                builder.setPushUrl("这里是请求的接口")//TODO 必填!!!!!!
                        .setHostCookie("s test=cookie String;")//cookie(只会初始化调用一次,后续上传不会再调用)
                        .setDebug(true)//是否是debug
                        .setSidPeriodMinutes(15)//sid改变周期
                        .setPushLimitMinutes(1)//多少分钟 push一次
                        .setPushLimitNum(100)//多少条 就主动进行push
                        .setCookieIntercept(new CookieFacade() {
                            @Override
                            public String getRequestCookies() { //宿主cookie通用参数 动态插入器(每次上传都会执行该方法)
                                return "cookie-->"+(++haha);
                            }
                        })
                        .start();//开始*/


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JJEventManager.destoryEventService();
                // EDBHelper.deleteEventListByLimit(0, 1);
                // ELogger.logWrite(TAG, "getEventListByRows-->" + EDBHelper.getEventRowCount());
            }
        });

        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JJEventManager.pushEvent();
                Toast.makeText(MainActivity.this,"saefsdfasdf-->"+i,Toast.LENGTH_SHORT).show();
            }
        });


    }
}
