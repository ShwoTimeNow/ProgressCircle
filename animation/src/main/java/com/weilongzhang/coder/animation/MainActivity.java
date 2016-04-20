package com.weilongzhang.coder.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.weilongzhang.coder.animation.paint.PaintActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onPaintClick(View view){
        startActivity(new Intent(this,PaintActivity.class));
    }


}
