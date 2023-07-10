package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private Button last;

    private Button test;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        test = findViewById(R.id.testBtn);
        tv = findViewById(R.id.test_text);
        last = findViewById(R.id.last);
        last.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_out,R.anim.right_in);
            finish();
        });
        final int[] counta = {0};
        final int[] countb = { 0 };
        final int[] countc = { 0 };
        final int[] count = {0};
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0]++;
                Random random = new Random();
                int a = random.nextInt(3); // 生成0、1、2中的随机数
                Toast.makeText(MainActivity2.this, "a:"+a, Toast.LENGTH_SHORT).show();
                if(a == 0){
                    counta[0]++;
                }else if(a == 1){
                    countb[0]++;
                } else{
                    countc[0]++;
                }
                tv.setText("总次数："+ count[0] +"，魏："+ counta[0] +"蜀："+ countb[0] +"吴："+ countc[0]);
            }
        });

    }
}