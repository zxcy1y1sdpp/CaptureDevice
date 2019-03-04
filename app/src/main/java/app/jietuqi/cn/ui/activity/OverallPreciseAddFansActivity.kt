package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.ContactEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.ContactUtil
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_precise_add_fans.*
import permissions.dispatcher.*


/**
 * 作者： liuyuanbo on 2018/11/11 11:54.
 * 时间： 2018/11/11 11:54
 * 邮箱： 972383753@qq.com
 * 用途： 精准加粉
 */
@RuntimePermissions
class OverallPreciseAddFansActivity : BaseOverallInternetActivity(), OnOptionsSelectListener {
    private var mList: ArrayList<OverallCardEntity> = arrayListOf()

    override fun setLayoutResourceId() = R.layout.activity_overall_precise_add_fans

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("精准加粉")
    }

    override fun initViewsListener() {
        mOverallPreciseAddFansAreaLayout.setOnClickListener(this)
        mOverallPreciseAddFansBusinessLayout.setOnClickListener(this)
        mOverallPreciseAddFansSexualityLayout.setOnClickListener(this)
        mOverallPreciseAddFansCountLayout.setOnClickListener(this)
        mOverallPreciseAddFansImportTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPreciseAddFansAreaLayout ->{//选择地区
                initAreaOptions()
            }
            R.id.mOverallPreciseAddFansBusinessLayout ->{//行业类别
                initIndustryOrGroup()

            }
            R.id.mOverallPreciseAddFansSexualityLayout ->{//选择性别
                initSingleOneOptionPicker(0)
            }
            R.id.mOverallPreciseAddFansCountLayout ->{//加粉数量
                initSingleOneOptionPicker(5)
            }
            R.id.mOverallPreciseAddFansImportTv ->{//导入通讯录
//                getCardsData()
            }
        }
    }
    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        super.onOptionsSelect(options1, options2, options3, v)
        when (mPickerType) {
            0 -> {
                val sex = v?.tag.toString()
                mOverallPreciseAddFansSexualityTv.text = sex
                when(sex){
                    "性别不限" ->{
                        mOverallPreciseAddFansSexualityTv.tag = ""
                    }
                    "男" ->{
                        mOverallPreciseAddFansSexualityTv.tag = "1"
                    }
                    else ->{
                        mOverallPreciseAddFansSexualityTv.tag = "2"
                    }
                }

            }
            1 -> {
                var industryEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
                mOverallPreciseAddFansBusinessTv.text = industryEntity.pickerViewText
                mOverallPreciseAddFansBusinessTv.tag = industryEntity.id
            }
            2 -> {
                var area = v?.tag.toString()
                mOverallPreciseAddFansAreaTv.text = area
                val arr=  area.split(" ")
                mOverallPreciseAddFansAreaLayout.tag = arr[0]
                mOverallPreciseAddFansAreaTv.tag = arr[1]
            }
            3 -> {
                var groupEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
                mOverallPreciseAddFansBusinessTv.text = groupEntity.pickerViewText
                mOverallPreciseAddFansBusinessTv.tag = groupEntity.id
            }
            4 ->{
                val number = v?.tag.toString()
                mOverallPreciseAddFansSexualityTv.text = v?.tag.toString()
                when(number){
                    "人数不限" ->{
                        mOverallPreciseAddFansSexualityTv.tag = ""
                    }
                    "<100人" ->{
                        mOverallPreciseAddFansSexualityTv.tag = "1"
                    }
                    ">100人" ->{
                        mOverallPreciseAddFansSexualityTv.tag = "2"
                    }
                }
            }
            5 ->{
                mLimit = v?.tag.toString()
                mOverallPreciseAddFansCountTv.text = v?.tag.toString()
            }
        }
    }
    @NeedsPermission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun getCardsData() {
        val postRequest = EasyHttp.post(HttpConfig.INFORMATION, true)
        postRequest.params("way", "lists")//way 必传add
                . params("limit", mLimit)
                . params("page", mPage.toString())
                .params("order", "update_time")
        if (mOverallPreciseAddFansSexualityTv.tag.toString().isNotBlank()){
            postRequest.params("sex", mOverallPreciseAddFansSexualityTv.tag.toString())
        }

        if (mOverallPreciseAddFansBusinessTv.tag.toString().isNotBlank()){
            postRequest.params("industry_id", mOverallPreciseAddFansBusinessTv.tag.toString())
        }
        if (mOverallPreciseAddFansAreaLayout.tag.toString().isNotBlank() && mOverallPreciseAddFansAreaTv.tag.toString().isNotBlank()){
            postRequest.params("province", mOverallPreciseAddFansAreaLayout.tag.toString())
            postRequest.params("district", mOverallPreciseAddFansAreaTv.tag.toString())
        }
        postRequest.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallCardEntity>>, ArrayList<OverallCardEntity>>(object : SimpleCallBack<ArrayList<OverallCardEntity>>() {
            override fun onSuccess(t: ArrayList<OverallCardEntity>) {
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                t.let { mList.addAll(it) }
                var contactList = arrayListOf<ContactEntity>()
                var entity: ContactEntity
                for (element in mList) {
                    entity = ContactEntity(element.mobile, element.wxnickname)
                    contactList.add(entity)
                }
                ContactUtil.batchAddContact(this@OverallPreciseAddFansActivity, contactList)
            }
            override fun onError(e: ApiException) {
                showToast(e.message)
            }

            override fun onStart() {
                super.onStart()
                showLoadingDialog()
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
                getCardsDataWithPermissionCheck()
            }
        }
    }
}
