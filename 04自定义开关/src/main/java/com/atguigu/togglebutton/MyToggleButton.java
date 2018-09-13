package com.atguigu.togglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

public class MyToggleButton extends View {

    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;
    /**
     * 距离左边最大距离
     */
    private int slidLeftMax;
    private Paint paint;
    private int slideLeft;

    private float startX;
    private float lastX;

    /**
     * 开关状态
     */
    private boolean isOpen = false;
    /**
     * true:点击事件生效，滑动事件不生效
     * false:点击事件不生效，滑动事件生效
     */
    private boolean isEnableClick = true;

    /**
     * 开关状态改变回调的接口
     */
    private OnOpenedChangeListener mOnOpenedChangeListener;

    public MyToggleButton(Context context) {
        this(context, null);
    }

    public MyToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyToggleButton, defStyleAttr, 0);
        int backgroundDrawableResId = a.getResourceId(R.styleable.MyToggleButton_switch_background, R.drawable.switch_background);
        int slideButtonDrawableResId = a.getResourceId(R.styleable.MyToggleButton_slide_button, R.drawable.slide_button);
        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundDrawableResId);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), slideButtonDrawableResId);
        slidLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slidingBitmap, slideLeft, 0, paint);
    }

    /**
     * 重写performClick()方法
     */
    @Override
    public boolean performClick() {
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    /**
     * 重写onTouchEvent(MotionEvent event)方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);//执行父类的方法
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录按下的坐标
                lastX = startX = event.getX();
                isEnableClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算结束值
                float endX = event.getX();
                //3.计算偏移量
                float distanceX = endX - startX;
                //4.屏蔽非法值
                correctDistanceX(distanceX);
                //5.刷新
                invalidate();
                //6.数据还原
                startX = event.getX();
                if (Math.abs(endX - lastX) > 5) {
                    //滑动状态不可点击
                    isEnableClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                //滑动状态
                if (!isEnableClick) {
                    if (slideLeft > slidLeftMax / 2) {
                        //显示按钮开
                        setOpened(true);
                    } else {
                        setOpened(false);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 切换点击的时候的状态
     */
    public void toggle() {
        if (isEnableClick) {
            setOpened(!isOpen);
        }
    }


    /**
     * 获取当前自定义开关的状态
     *
     * @return 当前自定义开关的状态
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 刷新
     */
    private void flushView() {
        if (isOpen) {
            slideLeft = slidLeftMax;
        } else {
            slideLeft = 0;
        }
        invalidate();//会导致onDraw()执行
    }


    /**
     * 纠正 slideLeft
     *
     * @param distanceX x轴滑动距离
     */
    private void correctDistanceX(float distanceX) {
        slideLeft += distanceX;
        if (slideLeft < 0) {
            slideLeft = 0;
        } else if (slideLeft > slidLeftMax) {
            slideLeft = slidLeftMax;
        }
    }

    /**
     * 开关状态改变回调的接口
     */
    public static interface OnOpenedChangeListener {
        /**
         * 当开关状态改变回调的时候回调该方法
         *
         * @param myToggleButton 自定义ToggleButton
         * @param isChecked      开关状态
         */
        void onOpenedChanged(MyToggleButton myToggleButton, boolean isChecked);
    }


    /**
     * 注册开关状态改变回调的接口
     *
     * @param listener 开关状态改变回调的接口
     */
    public void setOnOpenedChangeListener(OnOpenedChangeListener listener) {
        mOnOpenedChangeListener = listener;
    }

    /**
     * 状态改变
     *
     * @param opened 开关状态
     */
    public void setOpened(boolean opened) {
        isOpen = opened;
        flushView();
        if (mOnOpenedChangeListener != null) {
            mOnOpenedChangeListener.onOpenedChanged(this, isOpen);
        }
    }
}
