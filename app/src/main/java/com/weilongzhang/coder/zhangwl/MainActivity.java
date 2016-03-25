package com.weilongzhang.coder.zhangwl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.weilongzhang.coder.zhangwl.customview.ProgressCircle;
import com.weilongzhang.coder.zhangwl.customview.ProgressCircle.OnProgressChangeListener;

public class MainActivity extends AppCompatActivity {

    private ProgressCircle progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressCircle = (ProgressCircle) this.findViewById(R.id.rp);
        progressCircle.setProgress(85);
        progressCircle.setCanAnimation(true);
        progressCircle.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void progressChanging(String percent) {
                Log.d("",percent);
            }

            @Override
            public void onEndProgressChange(String endValue) {

            }

            @Override
            public String modifyValue(String value) {
                return super.modifyValue(value);
            }
        });
    }

    public void refresh(View view){
        progressCircle.setProgress((int)(Math.random()*100));
    }
}
