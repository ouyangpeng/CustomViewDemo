package com.atguigu.youku;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

/**
 * 作用：显示和隐藏指定控件
 * <p>
 * Android系统默认以顺时针旋转的
 * 旋转 圆心  x:getWidth()/2  y:getHeight()
 * 旋转 + 隐藏动画： 从 0  到 180  隐藏
 * 旋转 + 显示动画： 从180 到 360  显示
 */
public class Tools {
    /**
     * 隐藏View
     *
     * @param view 要隐藏的View
     */
    public static void hideView(ViewGroup view) {
        hideView(view, 0);
    }

    /**
     * 显示View
     *
     * @param view 要隐藏的View
     */
    public static void showView(ViewGroup view) {
        showView(view, 0);
    }

    /**
     * 隐藏View
     *
     * @param view        要隐藏的View
     * @param startOffset 动画延迟多久开始播放
     */
    public static void hideView(ViewGroup view, int startOffset) {
        //视图动画--补间动画
//        hideViewByRotateAnimation(view, startOffset);

        //视图动画--属性动画
        hideViewByObjectAnimation(view, startOffset);
    }

    /**
     * 显示View
     *
     * @param view        要显示的View
     * @param startOffset 动画延迟多久开始播放
     */
    public static void showView(ViewGroup view, int startOffset) {
        //视图动画--补间动画
//        showViewByRotateAnimation(view, startOffset);

        //视图动画--属性动画
        showViewByObjectAnimation(view, startOffset);
    }

    /**
     * 使用ObjectAnimation来隐藏ViewGroup
     *
     * @param viewGroup   要隐藏的ViewGroup
     * @param startOffset 动画延迟多久开始播放
     */
    private static void hideViewByObjectAnimation(ViewGroup viewGroup, int startOffset) {
        startObjectAnimationRotation(viewGroup, startOffset, 0, 180);
    }

    /**
     * 使用ObjectAnimation来显示ViewGroup
     *
     * @param viewGroup   要显示的ViewGroup
     * @param startOffset 动画延迟多久开始播放
     */
    private static void showViewByObjectAnimation(ViewGroup viewGroup, int startOffset) {
        startObjectAnimationRotation(viewGroup, startOffset, 180, 360);
    }

    /**
     * 开始播放 ObjectAnimation
     *
     * @param viewGroup   要显示动画的ViewGroup
     * @param startOffset 动画延迟多久开始播放
     * @param valueFrom   起始角度
     * @param valueTo     最终角度
     */
    private static void startObjectAnimationRotation(ViewGroup viewGroup, int startOffset, int valueFrom, int valueTo) {
        startObjectAnimation(viewGroup, startOffset, "rotation", 500, valueFrom, valueTo, viewGroup.getWidth() / 2, viewGroup.getHeight());
    }

    /**
     * 开始播放 ObjectAnimation
     *
     * @param viewGroup    要显示动画的ViewGroup
     * @param startOffset  动画延迟多久开始播放
     * @param propertyName 属性名称
     * @param duration     动画播放的时间
     * @param valueFrom    起始角度
     * @param valueTo      最终角度
     * @param pivotX       旋转的轴点和缩放的基准点坐标X
     * @param pivotY       旋转的轴点和缩放的基准点坐标Y
     */
    private static void startObjectAnimation(ViewGroup viewGroup, int startOffset, String propertyName, long duration, int valueFrom, int valueTo, float pivotX, float pivotY) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(viewGroup, propertyName, valueFrom, valueTo);
        //动画耗时
        animator.setDuration(duration);
        //动画延迟播放
        animator.setStartDelay(startOffset);
        animator.start();

        //设置旋转的轴点和缩放的基准点，默认是View的中心点
        viewGroup.setPivotX(pivotX);
        viewGroup.setPivotY(pivotY);
    }


    /**
     * 使用RotateAnimation来隐藏ViewGroup
     *
     * @param viewGroup   要隐藏的ViewGroup
     * @param startOffset 动画延迟多久开始播放
     */
    private static void hideViewByRotateAnimation(ViewGroup viewGroup, int startOffset) {
        RotateAnimation ra = new RotateAnimation(0, 180, viewGroup.getWidth() / 2, viewGroup.getHeight());
        // 开启动画并设置ViewGroup及其子View 不可以点击
        startRotateAnimation(viewGroup, startOffset, ra, false);
    }

    /**
     * 使用RotateAnimation来显示ViewGroup
     *
     * @param viewGroup   要显示的ViewGroup
     * @param startOffset 动画延迟多久开始播放
     */
    private static void showViewByRotateAnimation(ViewGroup viewGroup, int startOffset) {
        RotateAnimation ra = new RotateAnimation(180, 360, viewGroup.getWidth() / 2, viewGroup.getHeight());
        // 开启动画并设置ViewGroup及其子View 可以点击
        startRotateAnimation(viewGroup, startOffset, ra, true);
    }


    /**
     * 开启RotateAnimation动画
     *
     * @param viewGroup       要显示动画的ViewGroup
     * @param startOffset     动画延迟多久开始播放
     * @param rotateAnimation RotateAnimation动画
     * @param enable          是否可以点击，true为可以，false为不可以
     */
    private static void startRotateAnimation(ViewGroup viewGroup, int startOffset, RotateAnimation rotateAnimation, boolean enable) {
        startRotateAnimation(viewGroup, startOffset, 500, true, rotateAnimation, enable);
    }

    /**
     * 开启RotateAnimation动画
     *
     * @param viewGroup       要显示动画的ViewGroup
     * @param startOffset     动画延迟多久开始播放
     * @param durationMillis  动画播放持续的时间
     * @param fillAfter       动画停留在播放完成的状态
     * @param rotateAnimation RotateAnimation动画
     * @param enable          是否可以点击，true为可以，false为不可以
     */
    private static void startRotateAnimation(ViewGroup viewGroup, int startOffset, long durationMillis, boolean fillAfter, RotateAnimation rotateAnimation, boolean enable) {
        //设置动画播放持续的时间
        rotateAnimation.setDuration(durationMillis);
        //动画停留在播放完成的状态
        rotateAnimation.setFillAfter(fillAfter);
        //动画延迟多久开始播放
        rotateAnimation.setStartOffset(startOffset);
        //开启动画
        viewGroup.startAnimation(rotateAnimation);

        //因为基于View的渐变动画，只是改变了View的绘制效果，实际属性值并未改变。
        //比如：动画移动了一个按钮的位置，但是按钮的实际位置仍未改变。
        //所以在动画隐藏之后，需要设置ViewGroup及其子View不可以点击
        //    在动画显示之后，需要设置ViewGroup及其子View可以点击

        //设置ViewGroup及其子View是否可以点击
        setViewEnable(viewGroup, enable);
    }

    /**
     * 设置ViewGroup及其子View是否可以点击
     *
     * @param viewGroup 要设置是否可以点击的ViewGroup
     * @param enable    是否可以点击，true为可以，false为不可以
     */
    private static void setViewEnable(ViewGroup viewGroup, boolean enable) {
        viewGroup.setEnabled(enable);
        //循环设置ViewGroup的子View是否可以点击
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View children = viewGroup.getChildAt(i);
            children.setEnabled(enable);
        }
    }
}
