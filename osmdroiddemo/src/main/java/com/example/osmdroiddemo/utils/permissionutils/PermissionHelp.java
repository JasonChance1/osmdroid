package com.example.osmdroiddemo.utils.permissionutils;

import android.content.Context;
import android.content.Intent;

/**
 * 用于跳转到透明的activity
 */
public class PermissionHelp {
    private static PermissionHelp permissionTestHelp = null;
    private OnPermissionCallbackListener listener;

    public synchronized static PermissionHelp getInstance() {
        if (permissionTestHelp == null) {
            permissionTestHelp = new PermissionHelp();
        }
        return permissionTestHelp;
    }

    //重点，后面透明activity就是通过这个方法得到listener进行回调
    public OnPermissionCallbackListener getListener() {
        return listener;
    }

    public void requestPermissionsStart(Context context, OnPermissionCallbackListener listener, String... permissions) {
        this.listener = listener;
        Intent intent = new Intent(context, PermissionTransparentActivity.class);
        intent.putExtra("permissions", permissions);
        context.startActivity(intent);
    }
}
