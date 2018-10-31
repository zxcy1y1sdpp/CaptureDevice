package dasheng.com.capturedevice.database;

import android.content.Context;

/**
 * Created by 刘远博 on 2017/2/15.
 */

public class DatabaseUtils {
    private static MyOpenHelper mHelper;
    private DatabaseUtils(){}

    /**
     * 一般来说这里的initHelper放到application中去初始化
     * 当然也可以在项目运行阶段初始化
     */
    public static void initHelper(Context context){
        if(mHelper == null){
            mHelper = new MyOpenHelper(context);
        }
    }
    public static MyOpenHelper getHelper(){
        if(mHelper == null){
            new RuntimeException("MyOpenHelper is null,No init it");
        }
        return mHelper;
    }
}