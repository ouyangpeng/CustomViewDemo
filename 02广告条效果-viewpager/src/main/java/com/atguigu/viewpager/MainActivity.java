package com.atguigu.viewpager;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BaseHandlerCallBack {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;

    private ArrayList<CustomImageView> imageViews;

    /**
     * 图片资源ID
     */
    private final int[] imageIds = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5};

    /**
     * 图片标题集合
     */
    private final String[] imageDescriptions = {
            "美女1",
            "美女2",
            "美女3",
            "美女4",
            "美女5"
    };

    /**
     * 上一次高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;

    /**
     * Handler 防止内存泄漏
     */
    private NoLeakHandler mNoLeakHandler;

    private final int SEND_MESSAGE_DELAY_MILLIS = 4000;

    /**
     * 处理Handler消息
     *
     * @param msg Handler消息
     */
    @Override
    public void callBack(Message msg) {
        int item = viewpager.getCurrentItem() + 1;
        Log.d(TAG, "callBack() msg =" + msg  + " ,currentItem = " + item);
        viewpager.setCurrentItem(item);

        //延迟发消息
        mNoLeakHandler.sendEmptyMessageDelayed(0, SEND_MESSAGE_DELAY_MILLIS);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        //初始化要展示的数据
        imageViews = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            CustomImageView CustomImageView = new CustomImageView(this);
            CustomImageView.setBackgroundResource(imageIds[i]);
            CustomImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            //添加到集合中
            imageViews.add(CustomImageView);

            //添加点
            CustomImageView point = new CustomImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            int pxValue = DensityUtil.dip2px(MainActivity.this,8);//dp->px
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pxValue, pxValue);
            if (i == 0) {
                point.setEnabled(true); //显示红色
            } else {
                point.setEnabled(false);//显示灰色
                params.leftMargin = pxValue;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }

        viewpager.setAdapter(new MyPagerAdapter());
        //设置监听ViewPager页面的改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认设置中间位置，这样才能支持左右无限滑动
        //要保证imageViews的整数倍
        int item = Integer.MAX_VALUE / 2 - getRealPosition(Integer.MAX_VALUE / 2);

        // 22:   11 - 11 % 5 = 10       (5的倍数)
        // 400:  200 - 200 % 5 = 200    (5的倍数)
        // 803:  401 - 401 % 5 = 400    (5的倍数)

        viewpager.setCurrentItem(item);

        tv_title.setText(imageDescriptions[prePosition]);

        //发消息
        mNoLeakHandler = new NoLeakHandler(this);
        mNoLeakHandler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNoLeakHandler.removeCallbacksAndMessages(null);
    }

    /**
     * ViewPager页面滚动监听
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滚动了的时候回调这个方法
         *
         * @param position             当前页面的位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(TAG, MessageFormat.format("onPageScrolled() position ={0} ,positionOffset ={1} ,positionOffsetPixels = {2}",
                    position, positionOffset, positionOffsetPixels));
        }

        /**
         * 当某个页面被选中了的时候回调
         *
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected() position =" + position);
            int realPosition = getRealPosition(position);
            //设置对应页面的文本信息
            tv_title.setText(imageDescriptions[realPosition]);

            //把上一个高亮的设置默认-灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //当前的设置为高亮-红色
            ll_point_group.getChildAt(realPosition).setEnabled(true);
            prePosition = realPosition;
        }

        /**
         * 当页面滚动状态变化的时候回调这个方法
         *
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         * 静止->滑动
         * 滑动-->静止
         * 静止-->拖拽
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                mNoLeakHandler.removeCallbacksAndMessages(null);
                Log.i(TAG, "SCROLL_STATE_DRAGGING-------------------");
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                Log.i(TAG, "SCROLL_STATE_SETTLING-----------------");
            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {
                isDragging = false;
                Log.i(TAG, "SCROLL_STATE_IDLE------------");
                mNoLeakHandler.removeCallbacksAndMessages(null);
                mNoLeakHandler.sendEmptyMessageDelayed(0, SEND_MESSAGE_DELAY_MILLIS);
            }
        }
    }

    /**
     * ViewPager的适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        /**
         * 得到图片的总数
         *
         * @return 图片的总数
         */
        @Override
        public int getCount() {
//            return imageViews.size();
            //改为Integer.MAX_VALUE  支持无限滑动
            return Integer.MAX_VALUE;
        }

        /**
         * 相当于getView方法
         *
         * @param container ViewPager自身
         * @param position  当前实例化页面的位置
         * @return 当前位置对应的view
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int realPosition = getRealPosition(position);
            final CustomImageView CustomImageView = imageViews.get(realPosition);
            container.addView(CustomImageView);//添加到ViewPager中
            Log.d(TAG, "instantiateItem==" + position + ",---CustomImageView==" + CustomImageView);

            //设置图片的触摸事件
            CustomImageView.setOnTouchListener(new MyImageOnTouchListener());
            //保存位置
            CustomImageView.setTag(position);
            CustomImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getRealPosition((int) v.getTag());
                    String text = imageDescriptions[position];
                    Log.d(TAG, "点击事件 text=" + text);
                    Toast.makeText(MainActivity.this, "text=" + text, Toast.LENGTH_SHORT).show();
                }
            });

            return CustomImageView;
        }


        /**
         * 比较view和object是否同一个实例
         *
         * @param view   页面
         * @param object 这个方法instantiateItem返回的结果
         * @return view和object是否同一个实例的比较结果
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        /**
         * 释放资源
         *
         * @param container viewpager
         * @param position  要释放的位置
         * @param object    要释放的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            Log.d(TAG, "destroyItem==" + position + ",---object==" + object);
            container.removeView((View) object);
        }
    }

    class MyImageOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//          onTouch是优先于onClick的，
//          并且执行了两次，一次是ACTION_DOWN,一次是ACTION_UP(可能还会有多次ACTION_MOVE)，
//          因此事件传递的顺序是先经过OnTouch,再传递给onClick
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN://手指按下
                    Log.d(TAG, "onTouch==手指按下");
                    mNoLeakHandler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                    break;
                case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                    Log.d(TAG, "onTouch==事件取消");
                    break;
                case MotionEvent.ACTION_UP://手指离开
                    Log.d(TAG, "onTouch==手指离开");
                    mNoLeakHandler.removeCallbacksAndMessages(null);
                    mNoLeakHandler.sendEmptyMessageDelayed(0, SEND_MESSAGE_DELAY_MILLIS);
                    //如果底下的返回值为true,则需要调用performClick()方法，否则OnClick事件无效
                    //如果底下的返回值为false,则不一定需要调用performClick()方法
                    v.performClick();
                    break;
            }
            return true;
        }
    }

    /**
     * 获取真正的position
     *
     * @param position 传入的position
     * @return 真正的position
     */
    private int getRealPosition(int position) {
        return position % imageViews.size();
    }
}
