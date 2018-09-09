package com.atguigu.youku;

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
        hideViewByRotateAnimation(view, startOffset);

        //视图动画--属性动画
//        view.setRotation();

//        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,180);
//        animator.setDuration(500);
//        animator.setStartDelay(startOffset);
//        animator.start();
//
//        view.setPivotX(view.getWidth() / 2);
//        view.setPivotY(view.getHeight());
    }

    /**
     * 显示View
     *
     * @param view        要显示的View
     * @param startOffset 动画延迟多久开始播放
     */
    public static void showView(ViewGroup view, int startOffset) {
        showViewByRotateAnimation(view, startOffset);

//        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",180,360);
//        animator.setDuration(500);
//        animator.setStartDelay(startOffset);
//        animator.start();
//
//        view.setPivotX(view.getWidth() / 2);
//        view.setPivotY(view.getHeight());

    }

    /**
     * 使用RotateAnimation来隐藏View
     *
     * @param view        要隐藏的View
     * @param startOffset 动画延迟多久开始播放
     */
    private static void hideViewByRotateAnimation(ViewGroup view, int startOffset) {
        RotateAnimation ra = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight());
        startRotateAnimation(view, startOffset, ra);
        // 设置ViewGroup及其子View 不可以点击
        setViewEnable(view, false);
    }

    /**
     * 使用RotateAnimation来显示View
     *
     * @param view        要显示的View
     * @param startOffset 动画延迟多久开始播放
     */
    private static void showViewByRotateAnimation(ViewGroup view, int startOffset) {
        RotateAnimation ra = new RotateAnimation(180, 360, view.getWidth() / 2, view.getHeight());
        startRotateAnimation(view, startOffset, ra);
        // 设置ViewGroup及其子View 可以点击
        setViewEnable(view, true);
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

    /**
     * 开启RotateAnimation动画
     *
     * @param view        要显示动画的View
     * @param startOffset 动画延迟多久开始播放
     * @param ra          动画
     */
    private static void startRotateAnimation(ViewGroup view, int startOffset, RotateAnimation ra) {
        startRotateAnimation(view, startOffset, 500, true, ra);
    }

    /**
     * 开启RotateAnimation动画
     *
     * @param view           要显示动画的View
     * @param startOffset    动画延迟多久开始播放
     * @param durationMillis 动画播放持续的时间
     * @param fillAfter      动画停留在播放完成的状态
     * @param ra             动画
     */
    private static void startRotateAnimation(ViewGroup view, int startOffset, long durationMillis, boolean fillAfter, RotateAnimation ra) {
        //设置动画播放持续的时间
        ra.setDuration(durationMillis);
        //动画停留在播放完成的状态
        ra.setFillAfter(fillAfter);
        ra.setStartOffset(startOffset);
        view.startAnimation(ra);
    }
}
