package app.jietuqi.cn.wechat.simulator.widget;

/**
 * 作者： liuyuanbo on 2019/1/16 13:45.
 * 时间： 2019/1/16 13:45
 * 邮箱： 972383753@qq.com
 * 用途：
 */
/**
 10  * 震动监听器
 11  */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;



public class ShakeManager implements SensorEventListener {

    private Context mContext;
    private static final int SPEED_SHRESHOLD = 4500;//这个值越大需要越大的力气来摇晃手机
    private static final int UPTATE_INTERVAL_TIME = 50;
    private SensorManager sensorManager;
    private Sensor sensor;
    private ShakeListener shakeListener;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdateTime;

    public ShakeManager(Context c) {
        mContext = c;
        start();
    }

    /**
     * 启动
     */
    public void start() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        lastUpdateTime = currentUpdateTime;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
        if (speed >= SPEED_SHRESHOLD) {
            shakeListener.onShake();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void setShakeListener(ShakeListener shakeListener) {
        this.shakeListener = shakeListener;
    }
    public interface ShakeListener {
        void onShake();
    }
}
