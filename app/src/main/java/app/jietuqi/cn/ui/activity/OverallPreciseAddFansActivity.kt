package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.ConditionFinal
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import kotlinx.android.synthetic.main.activity_overall_precise_add_fans.*



/**
 * 作者： liuyuanbo on 2018/11/11 11:54.
 * 时间： 2018/11/11 11:54
 * 邮箱： 972383753@qq.com
 * 用途： 精准加粉
 */
class OverallPreciseAddFansActivity : BaseOverallInternetActivity(), OnOptionsSelectListener {
    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        if (mPickerType == 0){
            mOverallPreciseAddFansSexualityTv.text = ConditionFinal.SEXUALITY[options1]
        }
        if (mPickerType == 1){
            mOverallPreciseAddFansBusinessTv.text = ConditionFinal.BUSINESSTTYP[options1]
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_overall_precise_add_fans

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTitle("精准加粉")
    }



    override fun initViewsListener() {
        mOverallPreciseAddFansAreaLayout.setOnClickListener(this)
        mOverallPreciseAddFansBusinessLayout.setOnClickListener(this)
        mOverallPreciseAddFansSexualityLayout.setOnClickListener(this)
        mOverallPreciseAddFansCountTv.setOnClickListener(this)
        mOverallPreciseAddFansImportTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPreciseAddFansAreaLayout ->{//选择地区

            }
            R.id.mOverallPreciseAddFansBusinessLayout ->{//行业类别
                initSingleOneOptionPicker(1)

            }
            R.id.mOverallPreciseAddFansSexualityLayout ->{//选择性别
                initSingleOneOptionPicker()
            }
            R.id.mOverallPreciseAddFansCountTv ->{//加粉数量

            }
            R.id.mOverallPreciseAddFansImportTv ->{//导入通讯录

            }
        }
    }
}
