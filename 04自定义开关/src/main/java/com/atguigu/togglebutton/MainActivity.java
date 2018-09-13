package com.atguigu.togglebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyToggleButton myToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToggleButton = (MyToggleButton) findViewById(R.id.myToggleButton);
        myToggleButton.setOnOpenedChangeListener(new MyToggleButton.OnOpenedChangeListener() {
            @Override
            public void onOpenedChanged(MyToggleButton myToggleButton, boolean isChecked) {
                toast(isChecked);
            }
        });

        myToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              toast(myToggleButton.isOpen());
                toast(((MyToggleButton)v).isOpen());
            }
        });
    }

    private void toast(boolean open) {
        String toastMessage = open ? getString(R.string.opened) : getString(R.string.closed);
        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
    }
}
