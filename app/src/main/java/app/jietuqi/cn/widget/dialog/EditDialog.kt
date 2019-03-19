package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.ToastUtils
import kotlinx.android.synthetic.main.dialog_change_role.*

/**
 * @author lyb
 * * created at 17/6/29 下午7:45
 * * be used for 联系客服的弹框
 */
class EditDialog : DialogFragment(), View.OnClickListener {
    private var mEntity: EditDialogEntity? = null
    private var mListener: EditDialogChoiceListener? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cancelTv ->{}
            R.id.okTv ->{
                if (!TextUtils.isEmpty(OtherUtil.getContent(contentEt))){
                    mEntity?.content = contentEt.text.toString()
                    mListener?.onChoice(mEntity)
                }else{
                    ToastUtils.showShort(activity, "内容不能为空")
                }
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_change_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelTv.setOnClickListener(this)
        okTv.setOnClickListener(this)
        contentEt.setText(mEntity?.content)
        titleTv.text = mEntity?.title
        if (mEntity?.inputNumber == true){
            contentEt.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        }
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.setLayout((dm.widthPixels * 0.85).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 设置dialog中的数据
     */
    fun setData(listener: EditDialogChoiceListener?, entity: EditDialogEntity){
        mEntity = entity
        mListener = listener
    }
}
