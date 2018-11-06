package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.constant.RandomUtil
import dasheng.com.capturedevice.entity.eventbusentity.EventBusTimeEntity
import dasheng.com.capturedevice.util.EventBusUtil
import dasheng.com.capturedevice.util.OtherUtil
import dasheng.com.capturedevice.util.TimeUtil
import dasheng.com.capturedevice.wechat.db.WechatChargeHelper
import dasheng.com.capturedevice.wechat.entity.WechatChargeDetailEntity
import kotlinx.android.synthetic.main.activity_wechat_add_charge_detail.*
import kotlinx.android.synthetic.main.base_wechat_title.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/10/30 14:58.
 * 时间： 2018/10/30 14:58
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatAddChargeDetailActivity : BaseWechatActivity() {
    val mHelper: WechatChargeHelper = WechatChargeHelper(this)
    /**
     * 0 -- 收入
     * 1 -- 支出
     */
    private var mType = 0
    private var mEntity: WechatChargeDetailEntity = WechatChargeDetailEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_add_charge_detail

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        setWechatViewTitle("添加明细", 1)
        val time = TimeUtil.getNowAllTime()
        mTradingTimeTv.text = time
        mEntity.time = time
    }

    override fun initViewsListener() {
        mRefreshIv.setOnClickListener(this)
        mEarningTv.setOnClickListener(this)
        mExpenditureTv.setOnClickListener(this)
        mTradingTimeLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (null == intent.extras){
            sureTv.text = "确定"
        }else{
            mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatChargeDetailEntity
            sureTv.text = "修改"
            mTypeTv.setText(mEntity.name)
            mType = mEntity.type.toInt()
            if(mType == 0){
                OtherUtil.changeWechatTwoBtnBg(this, mEarningTv, mExpenditureTv)
            }else{
                OtherUtil.changeWechatTwoBtnBg(this, mExpenditureTv, mEarningTv)
            }
            mTradingTimeTv.text = mEntity.time
            mDetailMoneyEt.setText(mEntity.money)
        }

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mRefreshIv ->{
                if (mType == 0){//收入
                    mTypeTv.setText(RandomUtil.getRandomChargeEarningDetail().toString())
                }else{//支出
                    mTypeTv.setText(RandomUtil.getRandomChargeExpenditureDetail().toString())
                }
            }
            R.id.mTradingTimeLayout ->{
                initTimePickerView()
            }
            R.id.mEarningTv ->{
                mType = 0
                OtherUtil.changeWechatTwoBtnBg(this, mEarningTv, mExpenditureTv)
            }
            R.id.mExpenditureTv ->{
                mType = 1
                OtherUtil.changeWechatTwoBtnBg(this, mExpenditureTv, mEarningTv)
            }
            R.id.sureTv ->{
                if (TextUtils.isEmpty(mDetailMoneyEt.text.toString())){
                    Toast.makeText(this, "请填写金额", Toast.LENGTH_SHORT).show()
                    return
                }
                mEntity.name = mTypeTv.text.toString()
                mEntity.type = mType.toString()
                mEntity.money = mDetailMoneyEt.text.toString()
                var id = -1
                if (sureTv.text.toString() == "确定"){
                    id = mHelper.save(mEntity)
                    if (id > 0){
                        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "数据添加失败，请稍后失败！", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    id = mHelper.update(mEntity)
                    if (id > 0){
                        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "数据修改失败，请稍后失败！", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelecTimeEvent(timeEntity: EventBusTimeEntity) {
        mTradingTimeTv.text = timeEntity.time
        mEntity.time = timeEntity.time
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
