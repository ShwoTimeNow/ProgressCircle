package com.weilongzhang.coder.animation.value_animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.weilongzhang.coder.animation.R;

/**
 * Created by weilongzhang on 16/4/21.
 */
public class ValueAnimationActivity extends Activity {


    private ImageView iv1,iv2,iv3;
    private View v1,v2,v3;
    int[] int1,int2,int3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_value_animation);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        int1 = new int[2];
        int2 = new int[2];
        int3 = new int[2];

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);
            iv3.setVisibility(View.VISIBLE);

            v1.getLocationInWindow(int1);
            v2.getLocationInWindow(int2);
            v3.getLocationInWindow(int3);

            iv1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAnimation(iv1);
                    showAnimation(iv2);
                    showAnimation(iv3);
                }
            },1000);
        }
    }

    public void showAnimation(View view){
        ObjectAnimator translateXOA = ObjectAnimator.ofFloat(view,"translationX",0,10,-10,10,-5,5,-1,1,0);
        translateXOA.setDuration(200);
        translateXOA.setInterpolator(new AccelerateInterpolator());

        final AnimatorSet translationSet = new AnimatorSet();
        translationSet.playTogether(translateXOA);
        int[] position = new int[2];
        if (view == iv1){
            position = int1;
        }else if(view == iv2){
            position = int2;
        }else if(view == iv3){
            position = int3;
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"scaleX",1.0f,0.9f,0.8f,0.1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"scaleY",1.0f,0.9f,0.8f,0.1f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view,"pivotX",position[0]);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(view,"pivotY",position[1]);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimator,objectAnimator1,objectAnimator2,objectAnimator3);
        set.setDuration(500);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                translationSet.start();
            }
        });
        set.start();
    }

}
