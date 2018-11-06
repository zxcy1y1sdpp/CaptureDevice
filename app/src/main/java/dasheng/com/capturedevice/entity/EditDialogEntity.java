package dasheng.com.capturedevice.entity;

/**
 * 作者： liuyuanbo on 2018/11/2 10:59.
 * 时间： 2018/11/2 10:59
 * 邮箱： 972383753@qq.com
 * 用途： 编辑dialog的实体
 */

public class EditDialogEntity {
    public EditDialogEntity(int position, String content, String title){
        this.position = position;
        this.content = content;
        this.title = title;
    }
    /**
     * 是第几个点击显示的dialog，方便返回数据的时候进行区分
     */
    public int position;
    /**
     * dialog中输入框的内容
     */
    public String content;
    /**
     * dialog标题
     */
    public String title;
}
