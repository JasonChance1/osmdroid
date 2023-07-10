package com.example.osmdroiddemo.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.osmdroiddemo.R;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * 自定义更新界面
 */
public class CustomConfirmPopupView extends ConfirmPopupView {
    private Context context;
    private LinearLayout layoutBox;
    /**
     * @param context
     * @param bindLayoutId layoutId 要求布局中必须包含的TextView以及id有：tv_title，tv_content，tv_cancel，tv_confirm
     */
    public CustomConfirmPopupView(@NonNull Context context, int bindLayoutId) {
        super(context, bindLayoutId);
        this.context = context;
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return super.getImplLayoutId();
    }

    public CustomConfirmPopupView asCustomConfirm(CharSequence title, CharSequence content, CharSequence cancelBtnText, CharSequence confirmBtnText, OnConfirmListener confirmListener, OnCancelListener cancelListener, boolean isHideCancel){
        CustomConfirmPopupView popupView = new CustomConfirmPopupView(this.context, bindLayoutId);
        popupView.setTitleContent(title, content, null);
        popupView.setCancelText(cancelBtnText);
        popupView.setConfirmText(confirmBtnText);
        popupView.setListener(confirmListener, cancelListener);
        popupView.isHideCancel = isHideCancel;
        popupView.popupInfo = this.popupInfo;
        return popupView;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        layoutBox = findViewById(R.id.cancelBox);
        layoutBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
