
# JJEvent 数据埋点SDK


[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
![](https://img.shields.io/travis/rust-lang/rust/master.svg)
[![](https://img.shields.io/badge/release-1.0.0-brightgreen.svg)](https://github.com/ccj659/JJEvent/releases)
[![Author](https://img.shields.io/badge/autor-ccj659-brightgreen.svg)](https://github.com/ccj659)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)


# TO USE

## 1.SDK 服务启动


#### 1.1 `AndroidManifest.xml`添加网络访问权限

```
 <uses-permission android:name="android.permission.INTERNET"/>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

```

#### 1.2 在application中初始化

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //在applicaition中 将宿主中取通用cookie
        JJEventManager.init(this,"cookie String");

    }
}

```

#### 1.3 如果想进行事件统计,但是不想进行推送服务

```
/**
* 停止事件的上传任务(仍会记录事件,停止事件推送)
*/
JJEventManager.cancelEventPush();
```


#### 1.4 如果想立刻停止所有sdk的事件服务.

```
/**
* 停止事件的上传任务(仍会记录事件,停止事件推送)
*/
JJEventManager.destoryEventService();
```

#### 1.5 如果要进行参数设定,只需将`1.2` 中的代码改为如下即可.

```
    //
    //在applicaition中 将宿主中取通用cookie
        //JJEventManager.init(this,"s test=cookie String;",true);
            JJEventManager.Builder builder = new JJEventManager.Builder(getApplication());
                     builder.setHostCookie("s test=cookie String;")//cookie(1.只会初始化调用一次,后续上传不会再调用)
                             .setDebug(true)//是否是debug
                             .setSidPeriodMinutes(15)//sid改变周期
                             .setPushLimitMinutes(1)//多少分钟 push一次
                             .setPushLimitNum(100)//多少条 就主动进行push
                             .setCookieIntercept(new CookieFacade() {
                                 @Override
                                 public String getRequestCookies() { //宿主cookie通用参数 动态插入器(2.每次上传都会执行该方法,1.2两种取值方式,可按需选择一种)

                                     return getCookies();
                                 }
                             })
                             .start();//开始*/

```



## 2.统计操作


#### 2.1 PV事件(屏幕值)操作

```
//方式1:无自定义参数
JJEvent.screen("Android/首页/列表", LTPType.SCREEN_LTP_REFRESH);

//方式2: 有自定义参数
Map ecp = new HashMap();
ecp.put("自定义key1", "自定义value1");
ecp.put("自定义key2", "自定义value2");
JJEvent.screen("Android/首页/列表", LTPType.SCREEN_LTP_REFRESH, ecp);

```

#### 2.2 事件操作

```
//方式1: 无自定义参数
JJEvent.event("首页", "点击", "button");

//方式2: 添加自定义参数ecp
Map ecp = new HashMap();
ecp.put("自定义key1", "自定义value1");
ecp.put("自定义key2", "自定义value2");
JJEvent.event("首页", "点击", "button" , ecp);


```
#### 2.3 事件曝光

```
//方式1: 无自定义参数
JJEvent.expose("首页", "曝光", "button");

//方式2: 添加自定义参数ecp
Map ecp = new HashMap();
ecp.put("自定义key1", "自定义value1");
ecp.put("自定义key2", "自定义value2");
JJEvent.expose("首页", "曝光", "button" , ecp);


```

## ProGuard-rules

```
-keep class com.ccj.client.android.analytics.**{*;}
-keep class com.ccj.client.android.analytics.exception.**{*;}
-keep class com.ccj.client.android.analytics.enums.**{*;}
```






