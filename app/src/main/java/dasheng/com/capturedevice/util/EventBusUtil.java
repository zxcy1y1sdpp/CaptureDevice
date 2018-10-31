package dasheng.com.capturedevice.util;

import org.greenrobot.eventbus.EventBus;

/**
 * @author lyb
 * created at 2018/6/21 12:51 PM
 * be used for EventBus使用的工具类
 */
public class EventBusUtil {
	/**
	 * 注册接收器
	 * @param object
	 */
	public static void register(Object object){
		if (!EventBus.getDefault().isRegistered(object)){
			EventBus.getDefault().register(object);
		}
	}

	/**
	 * 注销接收器
	 * @param object
	 */
	public static void unRegister(Object object){
		EventBus.getDefault().unregister(object);
	}

	/**
	 * 注销粘性事件
	 * @param object
	 */
	public static void unRegisterSticky(Object object){
		EventBus.getDefault().removeStickyEvent(object);
		EventBus.getDefault().unregister(object);
	}
	public static void unRegisterSticky(Class<?> cls){
		EventBus.getDefault().removeStickyEvent(cls);
		EventBus.getDefault().unregister(cls);
	}

	public static void post(Object object){
		EventBus.getDefault().post(object);
	}
	public static void postSticky(Object object){
		EventBus.getDefault().postSticky(object);
	}
}
