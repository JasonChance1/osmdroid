package com.example.osmdroiddemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 权限BaseActivity封装
 * Author: Author
 * Date: 2017-08-23
 * Time: 9:27
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private onPermissionCallbackListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    /**
     * 申请权限
     *
     * @param permissions
     */
    public void onRequestPermission(String[] permissions, onPermissionCallbackListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        //1、 哪些权限需要申请
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //权限没有申请 添加到要申请的权限列表中
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //所有权限都同意了
            mListener.onGranted();
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            List<String> deniedPermissions = new ArrayList<>();
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permissions[i]);
                    }
                }
            }
            if (!deniedPermissions.isEmpty()) {
                mListener.onDenied(deniedPermissions);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showTipsDialog();
                    }
                }
            }else {
                mListener.onGranted();
            }
        } else {
            //所有的权限都被接受了
            mListener.onGranted();
        }
    }

    private void showTipsDialog() {
        // 跳转到应用设置界面
        new XPopup.Builder(this).asConfirm("权限不可用", "请在-应用设置-权限-中，允许使用权限", "取消", "立即开启", new OnConfirmListener() {
            @Override
            public void onConfirm() {
                Log.e("BaseActivity", "要开启进行权限设置");
                // 跳转到应用设置界面
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }, () -> {
            finish();
        },false).show();
    }


    /**
     * 权限处理接口
     */
    public interface onPermissionCallbackListener {
        void onGranted();

        void onDenied(List<String> deniedPermissions);
    }

}

