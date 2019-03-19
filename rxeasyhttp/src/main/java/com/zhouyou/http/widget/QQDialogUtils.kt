//package com.zhouyou.http.widget
//
//import android.content.Context
//
//import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
//
///**
// * 作者： liuyuanbo on 2019/3/14 16:13.
// * 时间： 2019/3/14 16:13
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//    class Singleton private constructor(str: String) {
//        var string: String = str;
//
//        init {
//            println("str is $str")
//            println("string is $string")
//        }
//
//        companion object {
//            @Volatile
//            var instance: Singleton? = null
//
//            fun getInstance(c: String): Singleton {
//                if (instance == null) {
//                    synchronized(Singleton::class) {
//                        if (instance == null) {
//                            instance = Singleton(c)
//                        }
//                    }
//                }
//                return instance!!
//            }
//        }
//    }
