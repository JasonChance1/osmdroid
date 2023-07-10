package com.example.osmdroiddemo.other;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.osmdroiddemo.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class XPopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpop);

        Button button = findViewById(R.id.asConfirm);
        Button button1 = findViewById(R.id.asInputConfirm);
        Button button2 = findViewById(R.id.asCenterList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(XPopActivity.this).asConfirm("标题", "1.内容\n2.内容\n3.内容3", "忽略此版本", "升级", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }
                }, () -> {

                },false,R.layout.custom_asconfirm).show();
            }
        });

        button1.setOnClickListener(v->{
//            new XPopup.Builder(XPopActivity.this)
//                    .popupPosition(PopupPosition.Right)
//                    .hasStatusBar(true)
//                    .asCustom(new CustomDrawerPopupView(XPopActivity.this))
//                    .show();
        });
    }
}