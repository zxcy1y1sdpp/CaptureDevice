package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.database.sqlite.SQLiteFullException
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.HaoDuanEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.ContactEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.ContactUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overal_number_add_fans.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/11/11 14:56.
 * 时间： 2018/11/11 14:56
 * 邮箱： 972383753@qq.com
 * 用途： 号段加粉
 */
@RuntimePermissions
class OverallNumberAddFansActivity : BaseOverallInternetActivity() {
    private var mSize = 10
    override fun setLayoutResourceId() = R.layout.activity_overal_number_add_fans

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("号段加粉", rightTitle = "号段查询")
    }

    override fun initViewsListener() {
        mOverallNumberLayoutAddBtn.setOnClickListener(this)
        mOverallNumberLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv->{
                LaunchUtil.startOverallWebViewActivity(this, "http://so.qqdna.com", "号段查询")
            }
            R.id.mOverallNumberLayout->{
                initSingleOneOptionPicker(6)
            }
            R.id.mOverallNumberLayoutAddBtn->{
                var phone = mOverallNumberAddFansEt.text.toString().trim()
                if (phone.length < 7){
                    showToast("请输入7位号段")
                    return
                }
                if (mSize == 0){
                    showToast("请选择加粉数量")
                    return
                }
                getPhoneNumbersWithPermissionCheck()
//                getPhoneNumbers()
            }
        }
    }


    /*private fun getPhoneNumbers(){

    }*/
    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        super.onOptionsSelect(options1, options2, options3, v)
        when (mPickerType) {
            6 ->{
                mOverallNumberTv.text = v?.tag.toString()
                mSize = v?.tag.toString().toInt()
            }
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun getPhoneNumbers() {
        var request: PostRequest = EasyHttp.post(HttpConfig.INDEX)
                .params("way", "random")
                .params("uid", UserOperateUtil.getUserId())
                .params("number", mSize.toString())
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<HaoDuanEntity>>, ArrayList<HaoDuanEntity>>(object : SimpleCallBack<ArrayList<HaoDuanEntity>>() {
            override fun onSuccess(phoneList: ArrayList<HaoDuanEntity>) {
//                val phoneList = t.split(",")
                var sEntity: ContactEntity
                var phone = mOverallNumberAddFansEt.text.toString().trim()
                var sList = arrayListOf<ContactEntity>()
                run {
                    var i = 0
                    val size = phoneList.size
                    while (i < size) {
                        sEntity = ContactEntity(phone + phoneList[i].phone, phone + phoneList[i].phone)
                        sList.add(sEntity)
                        i++
                    }
                }
                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    try {
                        ContactUtil.batchAddContact(this@OverallNumberAddFansActivity, sList)
                        runOnUiThread {
                            showToast("添加成功")
                        }
                    }catch (e: SQLiteFullException){
                        showToast("手机存储空间已满，无法执行操作！")
                    }

                }
            }

            override fun onStart() {
                super.onStart()
                showLoadingDialog("正在添加")
            }

            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }
        }) {})
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun showRationaleContacts(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun neverAskAgainContacts() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 通讯录 ] 访问权限")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.PERMISSION_NEED -> {
                getPhoneNumbersWithPermissionCheck()
            }
        }
    }
}
