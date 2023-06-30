package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private LinearLayout timebox;
    private Button btn,btn1,btn2,btn3;
    private Boolean flag = true;//显示或隐藏
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn_show);
        btn1 = findViewById(R.id.btn_show1);
        btn2 = findViewById(R.id.btn_show2);
        btn3 = findViewById(R.id.btn_show3);
        img = findViewById(R.id.imageView);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0, 1, 0, 1,0.5f,0.5f);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ScaleAnimation scaleAnimation3 = new ScaleAnimation(0, 1, 0, 1, Animation.ABSOLUTE, 0.5f, Animation.ABSOLUTE,0.5f);

        scaleAnimation.setDuration(2000);
        scaleAnimation.setStartOffset(0);
        scaleAnimation.setFillAfter(true);

        scaleAnimation1.setDuration(2000);
        scaleAnimation1.setStartOffset(0);
        scaleAnimation1.setFillAfter(true);

        scaleAnimation2.setDuration(2000);
        scaleAnimation2.setStartOffset(0);
        scaleAnimation2.setFillAfter(true);

        scaleAnimation3.setDuration(2000);
        scaleAnimation3.setStartOffset(0);
        scaleAnimation3.setFillAfter(true);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        btn.setOnClickListener(v -> {
            img.startAnimation(scaleAnimation);
        });
        btn1.setOnClickListener(v -> {
            img.startAnimation(scaleAnimation1);
        });
        btn2.setOnClickListener(v -> {
            img.startAnimation(scaleAnimation3);
        });

        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,90,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);

        // 透明动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0.5f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        // 平移动画
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,100,0,100);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.RELATIVE_TO_SELF,1,Animation.ABSOLUTE,0,Animation.RELATIVE_TO_SELF,1);
        translateAnimation.setDuration(2000);

        translateAnimation.setFillAfter(true);

        // 复合动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(rotateAnimation);

        AnimationDrawable ad = (AnimationDrawable) img.getBackground();

        btn3.setOnClickListener(v -> {
            if(flag){
                ad.start();
                flag = !flag;
                translateAnimation.setRepeatCount(-1);
                img.startAnimation(translateAnimation);
            }else{
                ad.stop();
                translateAnimation.setRepeatCount(0);
                flag = !flag;
            }

        });

        Button next;
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);

                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                finish();
            }
        });
    }
}