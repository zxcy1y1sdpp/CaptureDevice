package app.jietuqi.cn.lansong.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.File
import java.io.Serializable
import kotlin.properties.Delegates

/**
 * 作者： liuyuanbo on 2019/4/1 11:07.
 * 时间： 2019/4/1 11:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class LansongImgEntity : Serializable {
    var bitmap: Bitmap? = null
    var imgFilePath: String? = null
    var imgFile: File? = null
    var width: Float by Delegates.notNull()
    var height: Float by Delegates.notNull()
    /**
     * 在json文件中对应的id
     */
    var jsonImgId: String? = null
    /**
     * 在json文件中对应的文件名
     */
    var jsonImgName: String? = null

    /**
     * 蓝松需要用到的基本数据
     * @param jsonImgId
     * @param jsonImgName
     * @param width
     * @param height
     */
    constructor(jsonImgId: String, jsonImgName: String, width: Float, height: Float) {
        this.jsonImgId = jsonImgId
        this.jsonImgName = jsonImgName
        this.width = width
        this.height = height
    }
    constructor() {
    }

    /**
     * 通过文件名称创建对象
     * @param jsonImgId
     * @param jsonImgName
     * @param width
     * @param height
     * @param imgFilePath
     */
    constructor(jsonImgId: String, jsonImgName: String, width: Float, height: Float, imgFilePath: String) {
        this.jsonImgId = jsonImgId
        this.jsonImgName = jsonImgName
        this.width = width
        this.height = height
        this.imgFilePath = imgFilePath
        this.imgFile = File(imgFilePath)
        this.bitmap = BitmapFactory.decodeFile(imgFilePath)
    }

    /**
     * 通过文件对象创建对象
     * @param jsonImgId
     * @param jsonImgName
     * @param width
     * @param height
     * @param imgFile
     */
    constructor(jsonImgId: String, jsonImgName: String, width: Float, height: Float, imgFile: File) {
        this.jsonImgId = jsonImgId
        this.jsonImgName = jsonImgName
        this.width = width
        this.height = height
        this.imgFile = imgFile

        this.imgFilePath = imgFile.absolutePath
        this.imgFile = imgFile
        this.bitmap = BitmapFactory.decodeFile(this.imgFilePath)
    }

    companion object {
        private const val serialVersionUID = -6885682749947634656L
    }
}
