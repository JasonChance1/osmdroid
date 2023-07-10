package com.example.osmdroiddemo.utils.permissionutils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;


import com.example.osmdroiddemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 透明的activity，用于发起权限申请，获得权限申请结果并回调到上层（因以下原因，所以才有这个activity）
 * <p>
 * 权限申请结果必须在活动（activity/fragment）里面的获取的
 * 非活动类implements ActivityCompat.OnRequestPermissionsResultCallback
 * 也不能在onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)里面获取权限申请的结果
 */
public class PermissionTransparentActivity extends Activity {
    private int request_permissions_code = 1111;
    private String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = new View(this);
        view.setBackgroundResource(R.color.transparent);//透明背景
        setContentView(view);

        Intent intent = getIntent();
        if (intent != null) {
            permissions = intent.getStringArrayExtra("permissions");
            if (null == permissions || 0 == permissions.length) {
                finish();
            }
        } else {
            finish();
        }

        if (!EvPermissionUtils.checkSelPermission(this, permissions)) {//没有获取到权限
            ActivityCompat.requestPermissions(this, permissions, request_permissions_code);
        } else {//获取到权限了，那么直接关闭，若是不关闭就什么都点不了（尽量在上层获取到权限了都不用进来）
            finish();
        }
    }

    /**
     * 请求权限结果
     *
     * @param requestCode  请求的编码
     * @param permissions  请求的权限String[]
     * @param grantResults 请求权限String[]一一对应的结果int[](0代表授权成功，-1代表授权失败)
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == request_permissions_code) {//跟ActivityCompat.requestPermissions请求的requestCode要一致
            OnPermissionCallbackListener listener = PermissionHelp.getInstance().getListener();//这样子获得回调
            if (EvPermissionUtils.checkGrantResults(grantResults)) {//全部授权了
                listener.onGranted();//回调回去

            } else {//申请的权限没有全部被授权

//                List<String> grantedList = new ArrayList<>();//同意
                List<String> deniedList = new ArrayList<>();//拒绝
                List<String> askAgainList = new ArrayList<>();//拒绝且不再询问
                for (String permission : permissions) {
                    if (!EvPermissionUtils.checkSelPermission(this, permission)) {//没有授权
                        if (EvPermissionUtils.checkShouldShowRequestPermissionRationale(this, permission)) {//拒绝
                            deniedList.add(permission);
                        } else {//拒绝且不再询问
                            askAgainList.add(permission);
                        }
                    }else {
//                        grantedList.add(permission);
                    }
                }
//                if (grantedList != null && grantedList.size() > 0) {
//                    listener.onGranted(grantedList);//转成string[],这里只是同意部分权限,
//                }
                if (deniedList != null && deniedList.size() > 0) {
                    listener.onDenied(deniedList);
                }
                if (askAgainList != null && askAgainList.size() > 0) {
                    listener.onAskAgain(askAgainList);
                }
            }
        }
        finish();
    }
}