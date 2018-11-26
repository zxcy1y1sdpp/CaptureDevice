//package app.jietuqi.cn.widget;
//
//import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
//import com.bigkoo.pickerview.view.OptionsPickerView;
//
//import java.util.ArrayList;
//
//import app.jietuqi.cn.constant.ConditionFinal;
//
///**
// * 作者： liuyuanbo on 2018/11/25 15:39.
// * 时间： 2018/11/25 15:39
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//public class OptionsUtil {
//    public void initLocalDataOptions(int type, boolean showAllType) {//条件选择器初始化
//        ArrayList<String> list = new ArrayList<>();
//        OptionsPickerView optionPickerView;
//                mPickerType = type
//        OptionsPickerBuilder optionsPickerBuilder = new  OptionsPickerBuilder(this, this)
//                .setContentTextSize(20)//设置滚轮文字大小
//                .setLineSpacingMultiplier(2.0f)
//                .setSelectOptions(0, 1)//默认选中项
//                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//
//        when(type){
//            0 ->{//性别选择
//                optionsPickerBuilder.setTitleText("性别选择")
//                if (!showAllType){
//                    list.removeAt(0)
//                }
//            }
//            1 ->{
//                optionsPickerBuilder.setTitleText("行业类型选择")
//                list.clear()
//                list.addAll(ConditionFinal.BUSINESSTTYP)
//            }
//            3 ->{
//                optionsPickerBuilder.setTitleText("选择群类型")
//                list.clear()
//                list.addAll(ConditionFinal.GROUPTYPE)
//            }
//            4 ->{
//                optionsPickerBuilder.setTitleText("选择群人数")
//                list.clear()
//                list.addAll(ConditionFinal.GROUPPEOPLE)
//                if (!showAllType){
//                    list.removeAt(0)
//                }
//            }
//        }
//        optionPickerView = optionsPickerBuilder.build()
//        optionPickerView?.setPicker(list)//一级选择器
//        optionPickerView.show(View(this))
//    }
//}
