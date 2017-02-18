# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk-windows/tools/proguard/proguard-android.txt
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

#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontpreverify
#-dontskipnonpubliclibraryclasses
#-keepattributes *Annotation*
#-keepattributes *JavascriptInterface*
#-keep class android.webkit.JavascriptInterface {*;}
#-keepattributes Signature
#
#-keepattributes Signature
#
#-dontwarn com.google.gson.stream.**
#-dontwarn de.greenrobot.event.**
#-dontwarn de.greenrobot.**
#-dontwarn com.huewu.pla.lib.**
#-dontwarn Decoder.BASE64Encoder.**
#-dontwarn com.umeng.**
#-dontwarn com.unionpay.**
#-dontwarn org.apache.http.**
#-dontwarn javax.**
#-dontwarn java.nio.**
#-dontwarn com.android.volley.**
#-dontwarn butterknife.internal.**
#-keep class butterknife.internal.**{*;}
#-keep class com.google.gson.jpush.**{*;}
#-dontwarn com.google.gson.jpush.**
#-dontwarn com.squareup.okhttp.**
#
#-dontwarn org.codehaus.mojo.**
#
#-keep class org.apache.http.impl.client.**
#-dontwarn org.apache.commons.**
#
#-keepattributes Exceptions, Signature, InnerClasses
#
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.app.Fragment
#
#-keep public class * extends android.database.sqlite.**
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#
#-keep public class * extends android.app.Dialog
#-keep public class * extends android.view
#
#-keep class com.google.gson.jpush.**{*;}
#-dontwarn com.google.gson.jpush.**
#
#-keep class com.google.protobuf.jpush.**{*;}
#-dontwarn com.google.protobuf.jpush.**
#
#-keep class android.support.v4.** {*;}
#-keep class android.support.v7.** {*;}
#-keep class  android.support.design.**{*;}
#
##-keep class com.alibaba.fastjson.** { *; }
#-keep class com.umeng.** { *; }
#-keep class Decoder.BASE64Encoder.** { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class de.greenrobot.event.** { *; }
#
#-keep class android.support.design.widget.** { *; }
#-keep interface android.support.design.widget.** { *; }
#
#
#-keep class fpjk.nirvana.sdk.wjbridge.** { *; }
#
##ormlite
#-keep class com.j256.ormlite.** { *; }
#-keep class org.apache.commons.lang.** { *; }
#
#-keep class android.net.http.SslError
#-keep class android.webkit.**{*;}
#
#-dontwarn android.os.**
#-dontwarn com.android.internal.**
#-dontwarn cn.fraudmetrix.android.**
#-keepclasseswithmembers class cn.fraudmetrix.android.**
#
##oneAPM
#-keep class com.blueware.** { *; }
#-dontwarn com.blueware.**
#
##GrowingIo
#-dontwarn com.growingio.android.sdk.**
#-keep class com.growingio.android.sdk.** {
# *;
#}
#
##高德定位
#-dontwarn com.amap.api.location.**
#-dontwarn com.amap.api.fence.**
#-dontwarn com.autonavi.aps.amapapi.model.**
#-dontwarn com.loc.**
#-keep class com.amap.api.location.**{*;}
#-keep class com.amap.api.fence.**{*;}
#-keep class com.autonavi.aps.amapapi.model.**{*;}
#-keep class com.loc.**{*;}
##permision
#-dontwarn com.tbruyelle.rxpermissions.**
#-keep class com.tbruyelle.rxpermissions.**{*;}
#
##rxjava rxandroid
#-dontwarn rx.**
#-keep class rx.**{*;}
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keepclasseswithmembernames class * {
#    public <init> (android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembernames class * {
#    public <init> (android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.app.Activity {
#  public void *(android.view.View);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
#-keep public class **.R$* {
#    public static <fields>;
#}
#
#-keep public class fpjk.nirvana.sdk.wjbridge.R$*{
#	public static final int *;
#}
#
#-keepnames class * implements java.io.Serializable
#
#-keepclassmembers class * implements java.io.Serializable {
#   static final long serialVersionUID;
#   private static final java.io.ObjectStreamField[] serialPersistentFields;
#   !static !transient <fields>;
#   private void writeObject(java.io.ObjectOutputStream);
#   private void readObject(java.io.ObjectInputStream);
#   java.lang.Object writeReplace();
#   java.lang.Object readResolve();
#}
#
#-keepclassmembers class * {
#   public <init>(org.json.JSONObject);
#}