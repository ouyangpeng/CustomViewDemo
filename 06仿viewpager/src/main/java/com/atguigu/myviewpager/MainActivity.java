package com.atguigu.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private MyViewPager myviewpager;
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myviewpager = (MyViewPager) findViewById(R.id.myviewpager);

        //添加页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //添加到MyViewPager这个View中
            myviewpager.addView(imageView);
        }


    }
}
