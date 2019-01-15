package com.bm.zlzq.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import app.jietuqi.cn.App
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * 屏幕工具类--获取手机屏幕信息
 *
 * @author zihao
 */
object ScreenUtil {

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(context: Context): Int {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    val screenWidth: Int
        get() {
            val metric = DisplayMetrics()
            val wm = App.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metric)
            return metric.widthPixels
        }

    /**
     * 获取屏幕宽度用float作为单位
     * @return
     */
    val screenWidthWithFloat: Float
        get() {
            val manager = App.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            return display.width.toFloat()
        }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    fun getScreenHeight(context: Context): Int {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

    val screenHeight: Int
        get() {
            val metric = DisplayMetrics()
            val wm = App.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getRealMetrics(metric)
            return metric.heightPixels
        }

    /**
     * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
     *
     * @return
     */
    fun getScreenViewTopHeight(view: View): Int {
        return view.top
    }

    /**
     * 获取屏幕中控件底部位置的高度--即控件底部的Y点
     *
     * @return
     */
    fun getScreenViewBottomHeight(view: View): Int {
        return view.bottom
    }

    /**
     * 获取屏幕中控件左侧的位置--即控件左侧的X点
     *
     * @return
     */
    fun getScreenViewLeftHeight(view: View): Int {
        return view.left
    }

    /**
     * 获取屏幕中控件右侧的位置--即控件右侧的X点
     *
     * @return
     */
    fun getScreenViewRightHeight(view: View): Int {
        return view.right
    }

    /**
     * 将dp转化为px
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        try {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        } catch (throwable: Throwable) {
            // igonre
        }

        return 0
    }

    // 获取指定Activity的截屏，保存到png文件
    suspend fun takeScreenShot(activity: Activity): Bitmap {
        // View是你需要截图的View
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache

        // 获取状态栏高度
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        Log.i("TAG", "" + statusBarHeight)

        // 获取屏幕长和高
        val width = activity.windowManager.defaultDisplay.width
        val height = activity.windowManager.defaultDisplay.height
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        val b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return b
    }

    // 保存到sdcard
    fun savePic(b: Bitmap, strFileName: String = (Environment.getExternalStorageDirectory().absolutePath + "/zlzq/screenShoot/" + System.currentTimeMillis().toString() + ".jpg")): String{
        var path2 = strFileName
        var path = Environment.getExternalStorageDirectory().absolutePath + "/zlzq/screenShoot"
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        var fos: FileOutputStream
        try {
            fos = FileOutputStream(strFileName)
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.flush()
                fos.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return path2
    }
}