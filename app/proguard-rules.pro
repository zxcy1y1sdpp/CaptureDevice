# Add project specific ProGuard rules here.
# By default_head, the flags in this file are appended to flags specified
# in /Users/liuyuanbo/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#Glide混淆相关---------------------------------------------------------开始
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepnames class app.jietuqi.cn.base.GlobalGlideGonfig
# or more generally:
#-keep public class * implements com.bumptech.glide.module.GlideModule

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
-dontwarn com.squareup.picasso.**

#uCrop 相关
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

-keep class com.mob.**{*;}
-keep class cn.smssdk.**{*;}
-dontwarn com.mob.**
-dontwarn cn.smssdk.**

#Mob相关
#PaySDK已经做了混淆处理，再次混淆会导致不可预期的错误，请在您的混淆脚本中添加如下的配置，跳过对PaySDK的混淆操作：
-keep class com.mob.**{*;}
-dontwarn com.mob.**

