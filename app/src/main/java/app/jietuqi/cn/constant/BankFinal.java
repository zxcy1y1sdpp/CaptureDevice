package app.jietuqi.cn.constant;

import java.util.ArrayList;

import app.jietuqi.cn.R;
import app.jietuqi.cn.entity.BankEntity;

/**
 * 作者： liuyuanbo on 2018/10/23 14:44.
 * 时间： 2018/10/23 14:44
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class BankFinal {
    public static final ArrayList<BankEntity> BANK_LIST = new ArrayList();
    public static final String[] BANK_NAME = {"农业银行","中国银行","建设银行",
            "招商银行", "民生银行", "交通银行",
            "华夏银行", "工商银行", "邮政银行",
            "浦发银行", "农村信用社"};
    public static final int[] BANK_PIC = {R.mipmap.bank_abc, R.mipmap.bank_boc, R.mipmap.bank_ccb,
            R.mipmap.bank_cmb, R.mipmap.bank_cmbc, R.mipmap.bank_comm,
            R.mipmap.bank_hxbank, R.mipmap.bank_icbc, R.mipmap.bank_psbc,
            R.mipmap.bank_spdb, R.mipmap.bank_ydrcb};

    public static void initBankData(){
        if (BANK_LIST.size() != BANK_NAME.length && BANK_LIST.size() != BANK_PIC.length){
            BankEntity entity;
            for (int i = 0, size = BANK_NAME.length; i < size; i++) {
                entity = new BankEntity();
                entity.bankName = BANK_NAME[i];
                entity.bankPic = BANK_PIC[i];
                BANK_LIST.add(entity);
            }
        }
    }
}
