package app.jietuqi.cn.base

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.OtherUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhy.android.percent.support.PercentRelativeLayout
import kotlinx.android.synthetic.main.base_create_title.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/31 17:17.
 * 时间： 2018/10/31 17:17
 * 邮箱： 972383753@qq.com
 * 用途： 生成的基类
 */

abstract class BaseCreateActivity : BaseActivity(){
    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 右侧的确定按钮
     *        2 -- 微信单聊页面的三个点
     */
    protected fun setCreateTitle(title: String, type: Int = 0) {

        val titleTv = findViewById<TextView>(R.id.mBaseCreateTitleTv)
        when(type){
            1 ->{
                var sureTv = findViewById<TextView>(R.id.sureTv)
                sureTv.visibility = View.VISIBLE
                sureTv.setOnClickListener(this)
            }
            2 ->{
                var thirdPoint = findViewById<ImageView>(R.id.thirdPointIv)
                thirdPoint.visibility = View.VISIBLE
                thirdPoint.setOnClickListener(this)
            }
        }
        mBaseCreateFinishIv.setOnClickListener(this)
        titleTv.setOnClickListener(this)
        titleTv.text = title
    }

    /**
     * 微信收发红包预览中标题上的颜色
     * @param title
     */
    protected fun setWechatViewTitle(title: String, showRight: Boolean) {
        val sLeftIv = findViewById<ImageView>(R.id.mBaseCreateFinishIv)
        val titleTv = findViewById<TextView>(R.id.mBaseCreateTitleTv)
        sLeftIv.setOnClickListener(this)
        titleTv.text = title
        sLeftIv.setImageResource(R.drawable.wechat_preview_back)
        val sLayout = findViewById<PercentRelativeLayout>(R.id.wechatLayout)
        sLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.wechatPreviewTitleLayout))
        titleTv.setTextColor(ContextCompat.getColor(this, R.color.wechatPreviewTitle))
        if (showRight) {
            val sRightContentTv = findViewById<TextView>(R.id.wechatTitleRightTv)
            sRightContentTv.setTextColor(ContextCompat.getColor(this, R.color.wechatPreviewTitle))
            sRightContentTv.visibility = View.VISIBLE
        }
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.mBaseCreateFinishIv -> {
                onBackPressed()
                finish()
            }
            else -> {
            }
        }
    }

    /******************************************** 预览按钮相关 ********************************************/
    protected fun onlyOneEditTextNeedTextWatcher(inputEt: EditText){
//        val sPreviewBtn = findView<Button>(R.id.previewBtn)
        previewBtn.setOnClickListener(this)
        inputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt.text.toString().isNotEmpty()){
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })

    }
    protected fun onlyTowEditTextNeedTextWatcher(inputEt1: EditText, inputEt2: EditText){
//        val sPreviewBtn = findView<Button>(R.id.previewBtn)
        previewBtn.setOnClickListener(this)
        inputEt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt1.text.toString().isNotEmpty()){
                        if (inputEt2.text.toString().isNotEmpty()){
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                        }
                    } else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })
        inputEt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt2.text.toString().isNotEmpty()){
                        if (inputEt1.text.toString().isNotEmpty()){
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                        }
                    } else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })
    }
    protected fun onlyThreeEditTextNeedTextWatcher(inputEt1: EditText, inputEt2: EditText, inputEt3: EditText){
//        val sPreviewBtn = findView<Button>(R.id.previewBtn)
        previewBtn.setOnClickListener(this)
        inputEt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt1.text.toString().isNotEmpty()){
                        if (inputEt2.text.toString().isNotEmpty()){
                            if (inputEt3.text.toString().isNotEmpty()){
                                OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                            }
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                        }
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })
        inputEt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt2.text.toString().isNotEmpty()){
                        if (inputEt3.text.toString().isNotEmpty()){
                            if (inputEt1.text.toString().isNotEmpty()){
                                OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                            }
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                        }
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })
        inputEt3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt3.text.toString().isNotEmpty()){
                        if (inputEt2.text.toString().isNotEmpty()){
                            if (inputEt1.text.toString().isNotEmpty()){
                                OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, true)
                            }
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                        }
                    }else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseCreateActivity, previewBtn, false)
                }
            }
        })
    }

    /**
     * 选择时间
     * @param tag 防止一个页面有好多需要时间的地方无法区分
     * 注意：在调用该方法后，ondestory中要对eventbus进行销毁
     * @param type 0 -- 年月日时分秒， 1 -- 年月日时分，2 -- 分秒
     */
    fun initTimePickerView(tag: String = "", type: Int = 0, selectedDate: Calendar = Calendar.getInstance()){
        EventBusUtil.register(this)
        val startDate = Calendar.getInstance()
        //startDate.set(2013,1,1);
        val endDate = Calendar.getInstance()
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1)
        endDate.set(2020, 11, 31)

        var timePicker = TimePickerBuilder(this, OnTimeSelectListener { date, _ -> //选中事件回调
            var timeLong = getLongTime(date)
            var timeString = getStringTimeYMDHM(date)
            var timeOnlyMS = getStringTimeOnlyMS(date)
            var time = getStringTimeYMDHMS(date)
            val timeEntity = EventBusTimeEntity(timeLong, time, timeString, timeOnlyMS, tag)
            EventBusUtil.postSticky(timeEntity)
        }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setDividerColor(Color.RED)
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.LTGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setTextColorOut(Color.BLACK)
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        when (type) {
            0 -> timePicker.setType(booleanArrayOf(true, true, true, true, true, true))// 默认全部显示
            1 -> timePicker.setType(booleanArrayOf(true, true, true, true, true, false))// 默认全部显示
            2 -> timePicker.setType(booleanArrayOf(false, false, false, false, true, true))// 默认全部显示
        }

        var pvTime = timePicker.build()
        pvTime.show()
    }
    /**
     * 格式化时间格式
     * 计算时间戳只能根据这个方法
     * @param date
     * @return
     */
    private fun getLongTime(date: Date): Long {//可根据需要自行截取数据显示
        val time = date.time//Long类型变量
        return time
    }
    /**
     * 获取年-月-日- 时:分:秒
     */
    private fun getStringTimeYMDHMS(date: Date): String {//可根据需要自行截取数据显示
        var format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateString = format.format(date)
        return dateString
    }

    /**
     * 获取只有分:秒的时间
     */
    private fun getStringTimeOnlyMS(date: Date): String {//可根据需要自行截取数据显示
        var format: DateFormat = SimpleDateFormat("mm:ss")
        val dateString = format.format(date)
        return dateString
    }
}
