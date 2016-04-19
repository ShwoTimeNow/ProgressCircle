package com.weilongzhang.coder.zhangwl;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weilongzhang.coder.zhangwl.customview.ProgressCircleView;
import com.weilongzhang.coder.zhangwl.customview.ProgressCircleView2;

public class MainActivity extends AppCompatActivity {

    private ProgressCircleView progressCircle;
    private ProgressCircleView2 pc2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView =  (TextView) this.findViewById(R.id.tv_onclick);
        pc2 = (ProgressCircleView2) findViewById(R.id.pc2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"textview click!!!",Toast.LENGTH_LONG).show();
            }
        });
        progressCircle = (ProgressCircleView) this.findViewById(R.id.rp);
        progressCircle.setProgress(99);
        progressCircle.setCanAnimation(true);
        progressCircle.setOnProgressChangeListener(new ProgressCircleView.OnProgressChangeListener() {
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



        final int rightRate = 80;
        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        mValueAnimator.setDuration(500);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                int rate = (int) (rightRate * value);
                pc2.setAccuracy(rate);
            }
        });
        mValueAnimator.start();
    }

    public void refresh(View view){
        progressCircle.setProgress((int)(Math.random()*100));
    }
}
