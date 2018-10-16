package com.ccj.client.android.analytics.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * Created by chenchangjun on 18/2/11.
 */

public class EDeviceUtils {





    /**
     * 返回当前应用的版本名 如果出现错误则返1.0
     */
    public static String getAppVersionName(Context context) {
        String versionName = "0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            return "1.0";
        }
        return versionName;
    }

    /**
     * 获取当前进程名
     *
     * @param cxt
     * @param pid
     * @return
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = null;
        if (am != null) {
            runningApps = am.getRunningAppProcesses();
        }
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }



    /**
     * 获取默认传入服务器的User-Agent值
     *
     * @param context
     * @return
     */
    public static String getUserAgent(Context context) {
        if (context==null){
            return "";
        }

        String result = "app_jj";
        try {
            String packageName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
            String versionCode = String.valueOf(context.getPackageManager()
                    .getPackageInfo(packageName, 0).versionCode);
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            result = String.format(result, versionName, versionCode,
                    Build.MODEL, Build.VERSION.RELEASE, language);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Queen id 偏好键名
     * Shared preference key of Queen ID;
     */
    private static final String SHAREDPREF_NAME = "device";

    /**
     * Queen id 偏好键值
     * Shared preference name of Queen ID;
     */
    private static final String SHAREDPREF_KEY = "deviceId";


    /**
     * 检查是否具有获取某些设备信息的权限
     * Check ther permission required;
     *
     * @param context
     * @param permName 权限名; Permission name;
     * @return
     */
    public static boolean checkPermission(Context context, String permName) {
        return context.getPackageManager().checkPermission(permName, context.getPackageName())
                == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 获取Device model
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取生产商
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }




    /**
     * 获取APP的版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("JJEvent-->", "Cannot get app version name", e);
        }
        return versionCode;
    }




    /**
     * 获取包名
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取平台类型: Android
     *
     * @return
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 检查网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        boolean isNetworkConnected = false;

        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (null != networkInfo && networkInfo.isConnected());
    }

    /**
     * 获取Android的版本号
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId == null ? null : androidId.toUpperCase();
    }


    /**
     * 生成Queen ID.
     *
     * @return
     */
    private static String generateDeviceId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 保存Queen ID.
     *
     * @param deviceId Queen ID.
     * @param context
     */
    private static void saveDeviceId(String deviceId, Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE);

        preferences.edit().putString(SHAREDPREF_KEY, deviceId);
        preferences.edit().commit();

    }

    /**
     * 获取Queen ID;
     *
     * @param context
     * @return
     */
    private static String loadDeviceId(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE);

        return preferences.getString(SHAREDPREF_KEY, null);
    }

}
