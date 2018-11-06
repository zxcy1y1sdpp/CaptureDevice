package dasheng.com.capturedevice.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.callback.EditDialogChoiceListener
import dasheng.com.capturedevice.entity.EditDialogEntity
import kotlinx.android.synthetic.main.dialog_change_role.*

/**
 * @author lyb
 * * created at 17/6/29 下午7:45
 * * be used for 联系客服的弹框
 */
class EditDialog : DialogFragment(), View.OnClickListener {
    lateinit var mEntity: EditDialogEntity
    var mListener: EditDialogChoiceListener? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cancelTv ->{}
            R.id.okTv ->{
                mEntity.content = contentEt.text.toString()
                mListener?.onChoice(mEntity)
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.dialog_change_role, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelTv.setOnClickListener(this)
        okTv.setOnClickListener(this)
        contentEt.setText(mEntity.content)
        titleTv.text = mEntity.title
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            dialog.window.setLayout((dm.widthPixels * 0.75).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    /**
     * 设置dialog中的数据
     * @param title: dialog标题
     * @param content: 输入框中的内容
     * @param position: 是第几个点击显示的dialog，方便返回数据的时候进行区分
     */
    fun setData(listener: EditDialogChoiceListener, entity: EditDialogEntity){
        mEntity = entity
        mListener = listener
    }
}
