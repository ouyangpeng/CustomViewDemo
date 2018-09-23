package com.atguigu.myviewpager;

import android.os.SystemClock;

/**
 * 参考  android.widget.Scroller代码
 */
public class MyScroller {

    /**
     * X轴的起始坐标
     */
    private float startY;
    /**
     * Y轴的起始坐标
     */
    private float startX;

    /**
     * 在X轴要移动的距离
     */
    private int distanceX;
    /**
     * 在Y轴要移动的距离
     */
    private int distanceY;
    /**
     * 开始的时间
     */
    private long startTime;

    /**
     * 总时间
     */
    private long totalTime = 250;
    /**
     * 是否移动完成
     * false没有移动完成
     * true:移动结束
     */
    private boolean isFinish;

    /**
     * 当前x坐标
     */
    private float currX;

    /**
     * 得到坐标
     */
    public float getCurrX() {
        return currX;
    }

    /**
     * @param startX    X轴的起始坐标
     * @param startY    Y轴的起始坐标
     * @param distanceX 在X轴要移动的距离
     * @param distanceY 在Y轴要移动的距离
     */
    public void startScroll(float startX, float startY, int distanceX, int distanceY) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.startTime = SystemClock.uptimeMillis();//系统开机时间
        this.isFinish = false;
    }

    /**
     * 通过计算scroll滑动的距离，移动一小段对应的坐标，移动一小段对应的时间，判断是否还在移到
     *
     * @return true:正在移动 / false:移动结束
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }
        long endTime = SystemClock.uptimeMillis();
        //这一小段所花的时间
        long passTime = endTime - startTime;

        //还没有移动结束
        if (passTime < totalTime) {
            //计算平均速度
            float voleCity = distanceX / totalTime;
            //移动这个一小段对应的距离  (时间*速度)
            float distanceSmallX = passTime * voleCity;
            currX = startX + distanceSmallX;
        } else {
            //移动结束
            isFinish = true;
            currX = startX + distanceX;
        }
        return true;
    }
}
