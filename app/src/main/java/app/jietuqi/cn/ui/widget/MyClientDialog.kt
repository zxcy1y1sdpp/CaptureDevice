package app.jietuqi.cn.ui.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.ui.adapter.MyClientAdapter
import kotlinx.android.synthetic.main.dialog_wechat_simulator_select_bank.*
/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 */

class MyClientDialog : BottomSheetDialogFragment(){
    private var mAdapter: MyClientAdapter? = null
    private var mList = arrayListOf<OverallUserInfoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_my_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MyClientAdapter(mList)
        dialogRv.adapter = mAdapter
    }

    fun setData(list: ArrayList<OverallUserInfoEntity>){
        mList.addAll(list)
    }
}
