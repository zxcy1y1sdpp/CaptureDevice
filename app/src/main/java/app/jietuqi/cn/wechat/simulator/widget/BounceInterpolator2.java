package app.jietuqi.cn.wechat.simulator.widget;

import android.view.animation.Interpolator;

/**
 * 作者： liuyuanbo on 2019/3/19 19:21.
 * 时间： 2019/3/19 19:21
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class BounceInterpolator2 implements Interpolator {

    double mAmplitude = 1;//幅度
    double mFrequency = 10;//频率

    public BounceInterpolator2(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
