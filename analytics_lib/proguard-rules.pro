# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dontwarn
-ignorewarning
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-printmapping out.map

-keep class **.R$* {
 *;
}

-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}



-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保护代码中的Annotation不被混淆，这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*
#-keep class * extends java.lang.annotation.Annotation { *; } //注解可以混淆,正常使用

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*

# 避免混淆泛型，这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature

#抛出异常时保留代码行号，在异常分析中可以方便定位
-keepattributes SourceFile,LineNumberTable

-dontskipnonpubliclibraryclasses
#用于告诉ProGuard，不要跳过对非公开类的处理。默认情况下是跳过的，因为程序中不会引用它们，有些情况下人们编写的代码与类库中的类在同一个包下，并且对包中内容加以引用，此时需要加入此条声明。

-dontusemixedcaseclassnames
#，这个是给Microsoft Windows用户的，因为ProGuard假定使用的操作系统是能区分两个只是大小写不同的文件名，但是Microsoft Windows不是这样的操作系统，所以必须为ProGuard指定-dontusemixedcaseclassnames选项


#保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#对于带有回调函数的onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}


#-keep class com.ccj.client.android.analytics.net.gson.**{*;}
#-keep class com.ccj.client.android.analytics.net.core.**{*;}
#-keep class com.ccj.client.android.analytics.db.dbcore.**{*;}

#-keep class com.ccj.client.android.analytics.callback.**{*;}


#-keep class com.ccj.client.android.analytics.**


#-keep class com.ccj.client.android.analytics.JJEventManager* { *; }

-keep class com.ccj.client.android.analytics.bean.**{*;}
-keep class com.ccj.client.android.analytics.exception.**{*;}
-keep class com.ccj.client.android.analytics.enums.**{*;}

-keep public class com.ccj.client.android.analytics.JJEventManager* {
      public protected *;
}

-keep public class com.ccj.client.android.analytics.JJEvent* {
     public protected *;
}

-keep public class com.ccj.client.android.analytics.enums.LTPType* {
      public protected *;
}

#-keepclasseswithmembers public class com.ccj.client.android.analytics.JJEvent***

#-keepclassmembernames public class com.ccj.client.android.analytics.JJEvent {
#public *;
#}

#-keepnames class com.ccj.client.android.analytics.JJEvent{ ;}