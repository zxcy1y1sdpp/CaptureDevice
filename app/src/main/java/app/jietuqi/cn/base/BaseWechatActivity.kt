package app.jietuqi.cn.base

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.ChoicePaySheetDialog
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.zhouyou.http.EventBusUtil
import com.zhy.android.percent.support.PercentRelativeLayout
import kotlinx.android.synthetic.main.base_wechat_preview_title.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/3 21:57.
 * 时间： 2018/10/3 21:57
 * 邮箱： 972383753@qq.com
 * 用途： 微信相关的基类
 */

abstract class BaseWechatActivity : BaseActivity(), ChoicePaySheetDialog.OnItemChangeListener{
    override fun click(type: String) {
        if (type == "编辑角色"){

        }else{

        }
    }
    private var mPreviewBtn: Button? = null
    /**
     * 如果只有一个输入框需要监听
     */
    private var mInputEt: EditText? = null
    private var mWatcher: PreviewBtnStyle? = null

    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 右侧的确定按钮
     *        2 -- 微信单聊页面的三个点
     *        3 -- 微信零钱
     *        4 -- 标题变黑
     */
    protected fun setWechatViewTitle(title: String, type: Int = 0) {
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
            3 ->{
                wechatChangeDetail.visibility = View.VISIBLE
            }
            4 ->{
                mBaseCreateTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
        mBaseCreateFinishIv.setOnClickListener(this)
        mBaseCreateTitleTv.text = title
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
            R.id.previewBtn -> {

            }
            else -> {
            }
        }
    }

    /******************************************** 预览按钮相关 ********************************************/
    protected fun onlyOneEditTextNeedTextWatcher(inputEt: EditText, listener: View.OnClickListener){
        mInputEt = inputEt
        mWatcher = PreviewBtnStyle()
        mInputEt?.addTextChangedListener(mWatcher)
        needPreviewBtn(listener)
    }
    protected fun onlyTowEditTextNeedTextWatcher(inputEt1: EditText, inputEt2: EditText, listener: View.OnClickListener){
        mWatcher = PreviewBtnStyle()
        inputEt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt1.text.toString().isNotEmpty()){
                        if (inputEt2.text.toString().isNotEmpty()){
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, true)
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                        }
                    } else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                }
            }
        })
        inputEt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    if (inputEt2.text.toString().isNotEmpty()){
                        if (inputEt1.text.toString().isNotEmpty()){
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, true)
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                        }
                    } else{
                        OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                    }
                } else {
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, previewBtn, false)
                }
            }
        })
//        needPreviewBtn(listener)
        previewBtn.setOnClickListener(listener)
    }
    protected fun needPreviewBtn(listener: View.OnClickListener) {
        mPreviewBtn = findViewById(R.id.previewBtn)
        mPreviewBtn?.setOnClickListener(listener)
    }
    inner class PreviewBtnStyle : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (s.length > 0) {
                if (mInputEt?.text.toString().length >0){
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, mPreviewBtn, true)
                } else{
                    OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, mPreviewBtn, false)
                }
            } else {
                OtherUtil.changeWechatPreviewBtnBg(this@BaseWechatActivity, mPreviewBtn, false)
            }
        }
    }

    override fun onDestroy() {
        if (null != mWatcher){
            mInputEt?.removeTextChangedListener(mWatcher)
            mWatcher = null
            mInputEt = null
        }
        if (null != mPreviewBtn){
            mPreviewBtn = null
        }
        super.onDestroy()
    }

    /**
     * 选择时间
     * @param tag 防止一个页面有好多需要时间的地方无法区分
     * 注意：在调用该方法后，ondestory中要对eventbus进行销毁
     * @param type 0 -- 年月日时分秒， 1 -- 年月日时分，2 -- 分秒
     */
    fun initTimePickerView(tag: String = "", type: Int = 0){
        EventBusUtil.register(this)
        val selectedDate = Calendar.getInstance()
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
