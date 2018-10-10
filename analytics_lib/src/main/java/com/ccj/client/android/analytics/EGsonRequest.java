package com.ccj.client.android.analytics;


import com.ccj.client.android.analytics.intercept.CookieFacade;
import com.ccj.client.android.analytics.net.core.AuthFailureError;
import com.ccj.client.android.analytics.net.core.IntegerAdapter;
import com.ccj.client.android.analytics.net.core.NetworkResponse;
import com.ccj.client.android.analytics.net.core.ParseError;
import com.ccj.client.android.analytics.net.core.Request;
import com.ccj.client.android.analytics.net.core.Response;
import com.ccj.client.android.analytics.net.core.Tools.HttpHeaderParser;
import com.ccj.client.android.analytics.net.gson.EGson;
import com.ccj.client.android.analytics.net.gson.GsonBuilder;
import com.ccj.client.android.analytics.net.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class EGsonRequest<T> extends Request<T> {
    private static final String TAG_GSON ="TAG_GSON" ;
    public static CookieFacade cookieIntercept;
    private final Class<T> clazz;
    private Map<String, String> headers = new HashMap<>();
    private final Response.Listener<T> listener;
    private final Map<String, String> params;
   private String url_log = "";

    private static final EGson gson = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerAdapter())
            .registerTypeAdapter(int.class, new IntegerAdapter())
            .registerTypeAdapter(Long.class, new IntegerAdapter())
            .registerTypeAdapter(long.class, new IntegerAdapter())
            .create();

    private String cookie="";//从宿主app中获取cookie,每次刷新

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to makeb
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public EGsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
                        Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        if(method==Method.POST){
            this.params=addPostParams(params);
        }else {
            this.params = params;
        }
        this.url_log = url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault( manager  );
        //将cookie设置为 外部引入

        try {
            if (cookieIntercept!=null){ //采用动态cookie注入,则采用注入方式
                cookie= cookieIntercept.getRequestCookies();
            }else {//否则, 采用静态cookie注入
                cookie=EventDecorator.getRequestCookies();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (headers == null)
            headers = new HashMap<>();
        headers.put("Cookie", cookie);

        ELogger.logWrite(TAG_GSON,"headers-->"+headers);
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        if (EConstant.DEVELOP_MODE) {
            listener.onResponse(response);
        } else {
            try {
                listener.onResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
            ELogger.logWrite(TAG_GSON, url_log + "return:\n" + json);
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            ELogger.logWrite(TAG_GSON, url_log + "return:\n" + "解析失败:UnsupportedEncodingException:" + e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            ELogger.logWrite(TAG_GSON, url_log + "return:\n" + "解析失败:JsonSyntaxException:" + e.getMessage());
            return Response.error(new ParseError(e));
        }catch (Exception e){
            ELogger.logWrite(TAG_GSON, url_log + "return:\n" + "GsonRequest错误未定义:" + e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

}