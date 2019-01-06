package com.luoruiyong.fileshare.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseActivity;
import com.luoruiyong.fileshare.profile.view.DownloadFileFragment;
import com.luoruiyong.fileshare.profile.view.MyShareFileFragment;

public class ProfileActivity extends BaseActivity {

    private static final String ACTIONBAR_TITLE = "title";
    public static final String ACTIONBAR_TITLE_DOWNLOAD_FILE = "已下载文件";
    public static final String ACTIONBAR_TITLE_MY_SHARE_FILE = "我的共享文件";

    private Toolbar mToolbar;
    private String mTitle;

    public static void startAction(Context context, String title) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(ACTIONBAR_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        initToolbar();

        // 检查权限
        if (!isPermissionGranted()) {
            // 请求权限
            requestPermissions(new OnRequestPermissionsResultCallBack() {
                @Override
                public void onGranted() {
                    initFragment();
                }

                @Override
                public void onDenied() {
                    // 用户拒绝授权，退出回到首页
                    Toast.makeText(ProfileActivity.this, "您拒绝了应用获取设备的文件读写权限，无法正常扫描相关文件", Toast.LENGTH_LONG).show();
                    ProfileActivity.this.finish();
                }
            });
        } else {
            // 已获得权限，初始化Fragment
            initFragment();
        }

    }

    private void initToolbar() {
        mTitle = getIntent().getStringExtra(ACTIONBAR_TITLE);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        }
    }

    private void initFragment() {
        // 通过传进来的title参数来决定展示那一个页面
        Fragment fragment = null;
        switch (mTitle) {
            case ACTIONBAR_TITLE_DOWNLOAD_FILE:
                fragment = new DownloadFileFragment();
                break;
            case ACTIONBAR_TITLE_MY_SHARE_FILE:
                fragment = new MyShareFileFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fragment, mTitle).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ProfileActivity.this.finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getSupportFragmentManager().findFragmentByTag(mTitle).onActivityResult(requestCode, resultCode, data);
    }
}
