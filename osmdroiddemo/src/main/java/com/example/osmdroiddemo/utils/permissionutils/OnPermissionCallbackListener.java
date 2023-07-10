package com.example.osmdroiddemo.utils.permissionutils;

import java.util.List;

public interface OnPermissionCallbackListener {
    void onGranted();
    void onDenied(List<String> deniedPermissions);

    void onAskAgain(List<String> asPermissions);
}