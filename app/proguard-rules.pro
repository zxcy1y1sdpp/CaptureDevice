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
-keepnames class dasheng.com.capturedevice.base.GlobalGlideGonfig
# or more generally:
#-keep public class * implements com.bumptech.glide.module.GlideModule

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#Glide混淆相关---------------------------------------------------------结束
-dontwarn com.squareup.picasso.**

#uCrop 相关
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
