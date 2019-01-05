package com.luoruiyong.fileshare.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private OnRequestPermissionsResultCallBack mResultCallback;
    private boolean mFinished = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFinished = true;
    }

    protected boolean isFinishingOrFinished() {
        return isFinishing() || mFinished;
    }

    public boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(OnRequestPermissionsResultCallBack callBack) {
        mResultCallback = callBack;
        if (ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            if (mResultCallback != null) {
                mResultCallback.onGranted();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mResultCallback != null) {
                        mResultCallback.onGranted();
                    }
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // 用户拒绝了权限，并勾选了不再提示
                        showExplainPermissionDialog();
                    } else {
                        // 用户拒绝权限
                        if (mResultCallback != null) {
                            mResultCallback.onDenied();
                        }
                    }
                }
                break;
        }
    }

    public void showExplainPermissionDialog() {
        if (isFinishingOrFinished()) {
            return;
        }
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("权限说明")
                .setMessage("应用上传/下载共享文件需要获取设备的文件读取权限,请前往设置。")
                .setCancelable(true)
                .setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //引导用户至设置页手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("暂不设置", null)
                .show();
    }

    public void showTipPermissionDialog() {
        if (isFinishingOrFinished()) {
            return;
        }
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle("温馨提示")
                .setMessage("应用上传/下载共享文件需要获取设备的文件读取权限,请同意授权。")
                .setCancelable(true)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //引导用户至设置页手动授权
                       requestPermissions(null);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public interface OnRequestPermissionsResultCallBack {
        void onGranted();
        void onDenied();
    }
}
