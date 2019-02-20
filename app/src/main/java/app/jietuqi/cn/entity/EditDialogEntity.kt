package app.jietuqi.cn.entity

/**
 * 作者： liuyuanbo on 2018/11/2 10:59.
 * 时间： 2018/11/2 10:59
 * 邮箱： 972383753@qq.com
 * 用途： 编辑dialog的实体
 */

class EditDialogEntity (
        /**
         * 是第几个点击显示的dialog，方便返回数据的时候进行区分
         */
        var position: Int = -1,
        /**
         * dialog中输入框的内容
         */
        var content: String = "",
        /**
         * dialog标题
         */
        var title: String,
        /**
         * 是否必须是数字
         */
        var inputNumber: Boolean = false)
