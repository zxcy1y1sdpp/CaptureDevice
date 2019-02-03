//package app.jietuqi.cn.wechat.simulator.widget;
//
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//
///**
// * 作者： liuyuanbo on 2019/1/16 14:16.
// * 时间： 2019/1/16 14:16
// * 邮箱： 972383753@qq.com
// * 用途：
// */
//public class ShakeSensorListener implements SensorEventListener {
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        //避免一直摇
//        if (isShake) {
//            return;
//        }
//        // 开始动画
//        anim.start();
//        float[] values = event.values;
//        /*
//         * x : x轴方向的重力加速度，向右为正
//         * y : y轴方向的重力加速度，向前为正
//         * z : z轴方向的重力加速度，向上为正
//         */
//        float x = Math.abs(values[0]);
//        float y = Math.abs(values[1]);
//        float z = Math.abs(values[2]);
//        //加速度超过19，摇一摇成功
//        if (x > 19 || y > 19 || z > 19) {
//            isShake = true;
//            //播放声音
//            playSound(MainActivity.this);
//            //震动，注意权限
//            vibrate( 500);
//            //仿网络延迟操作，这里可以去请求服务器...
//            newfun Handler().postDelayed(newfun Runnable() {
//                @Override
//                public void run() {
//                    //弹框
//                    showDialog();
//                    //动画取消
//                    anim.cancel();
//                }
//            },1000);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }
//}
