package com.example.osmdroiddemo.utils.permissionutils;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/weixin_40420578/article/details/131215299
 */
public class EvPermissionUtils {
    private static String TAG = EvPermissionUtils.class.getSimpleName();

    /**
     * 校验权限是否都授权（只要有一个没有授权就是false）
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkSelPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 判断sdk版本
            return true;
        }
        for (String permission : permissions) {
            //没有授权：PackageManager.PERMISSION_DENIED；已授权：PackageManager.PERMISSION_GRANTED；用PERMISSION_GRANTED来判断
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 校验权限是否都授权
     *
     * @param context
     * @param permissions
     * @return 返回没有授权的string[]
     */
    public static String[] checkSelPermissionStr(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 低于android 6 无需授权
            return null;
        }

        // 没有被授权的列表
        List<String> deniedList = new ArrayList<>();

        // 循环遍历所申请的所有权限，看看是否都申请到了;但凡有一个没有申请，那就立即去申请
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {//这个权限没有申请到
                deniedList.add(permission);
            }
        }

        // 把没授权的列表转成数组
        if (deniedList != null || deniedList.size() > 0) {
            String[] permissionDenied = new String[deniedList.size()];
            for (int i = 0; i < deniedList.size(); i++) {
                permissionDenied[i] = deniedList.get(i);
            }
            return permissionDenied;
        } else {
            return null;
        }
    }

    /**
     * 检验权限结果是否都授权（onRequestPermissionsResult()里面调用）
     *
     * @param grantResults
     * @return 有一个没有授权成功就返回false
     */
    public static boolean checkGrantResults(int[] grantResults) {
        boolean grant = true;
        if (grantResults != null && grantResults.length > 0) {
            for (int grantResult : grantResults) {
                //没有授权：PackageManager.PERMISSION_DENIED；已授权：PackageManager.PERMISSION_GRANTED
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    grant = false;
                    break;
                }
            }
        }
        return grant;
    }

    /**
     * 判断用户是否勾选不再提示 返回false
     * true:之前请求过此权限但拒绝了，但是没有勾选不再提示;
     * false:1.之前请求拒绝了且勾选不再提示(那就不能再提示了);2.之前没有拒绝过即第一次申请(两种情况都返回false)
     * 所以在onRequestPermissionsResult（）方法里面用，不在requestPermissions（）申请权限前用，因无法判断false具体是哪个
     */
    public static boolean checkShouldShowRequestPermissionRationale(Activity activity, @NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 权限申请统一入口
     * @param context
     * @param listener
     * @param permissions
     */
    public static void requestPermissions(Context context, OnPermissionCallbackListener listener, String... permissions) {
        String[] deniedPermission = checkSelPermissionStr(context, permissions);
        if (deniedPermission != null && deniedPermission.length > 0) {
            //优化：写个弹窗，告诉用户申请这些权限是用来做什么的（只提示没有授权的），当用户点击确定后再requestPermissionsStart，点击取消后可回调拒绝或不处理
            new XPopup.Builder(context)
                    .asConfirm("权限不可用", "请在-应用设置-权限-中允许使用权限", "取消", "立即开启",
                            () -> {
                                // 跳转到应用设置界面
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                requestPermissionsStart(context,listener,deniedPermission);
                                startActivity(intent);
                            }, () -> {

                            },false).show();


        } else {
            listener.onGranted();
        }
    }

    /**
     * 权限申请
     * @param context
     * @param listener
     * @param permissions
     */
    public static void requestPermissionsStart(Context context, final OnPermissionCallbackListener listener, final String... permissions) {
        //授权成功，拒绝，拒绝且不再询问的逻辑处理都由上层处理，则使用这个
//        PermissionHelp.getInstance().requestPermissionsStart(context,listener,permissions);

        /**
         *统一处理：拒绝且不再询问，弹窗引导用户去设置，用这个（推荐）
         * 那么修改传进来的Listener就不要用PermissionTestListener，换个**Listener
         * 上层只处理授权成功的业务那**Listener里面只实现onGranted方法
         * 处理授权成功和拒绝的业务**Listener里面实现onGranted、onDenied两个方法
         * 注：onDenied、onAskAgain某些情况会回都回调，申请多个权限ABCD，第一次我拒绝且不再询问权限A，后面拒绝BCD任何一个，
         * 所以上层一般不处理拒绝的回调，统一处理“拒绝且不再询问”后去引导用户设置
         */
        PermissionHelp.getInstance().requestPermissionsStart(context, new OnPermissionCallbackListener() {

            @Override
            public void onGranted() {
                listener.onGranted();
            }

            @Override
            public void onDenied(List<String> dePermissions) {
                Log.i(TAG, "拒绝：" + dePermissions.size());
                listener.onDenied(dePermissions);
            }

            @Override
            public void onAskAgain(List<String> asPermissions) {
                Log.i(TAG, "拒绝且不再询问：" + asPermissions.size());
//                new XPopup.Builder(context)
//                        .asConfirm("权限不可用", "必须开启权限，否则影响使用", "取消", "立即开启",
//                                () -> {
////                                // 跳转到应用设置界面
//                                Intent intent = new Intent();
//                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                                intent.setData(uri);
//                                startActivity(intent);
//                                }, () -> {
//
//                                },true).show();
//                listener.onAskAgain(asPermissions);
            }
        }, permissions);
    }
}
