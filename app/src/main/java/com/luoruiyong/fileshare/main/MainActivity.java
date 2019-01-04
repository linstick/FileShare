package com.luoruiyong.fileshare.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseListFragment;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.main.view.HostListFragment;
import com.luoruiyong.fileshare.main.view.ShareFileListFragment;

public class MainActivity extends AppCompatActivity implements HostListFragment.OnHostItemClickListener {

    private static final String TAG = "MainActivity";
    private static long sLastBackPressTime = 0;
    private final String HOST_TAG = "host";
    private final String SHARE_FILE_TAG = "share_file";
    private final String ACTIONBAR_TITLE_DEFAULT = "共享小助手";
    private final String ACTIONBAR_TITLE_HOST_LIST = "共享主机";
    private final String ACTIONBAR_TITLE_FILE_LIST = "共享文件";
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String mCurrentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        }

        mCurrentTag = HOST_TAG;
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new HostListFragment(), HOST_TAG).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                browseDownloadHistory();
                break;
            case R.id.menu_profile:
                browseProfile();
                break;
            case R.id.menu_refresh:
                refresh();
                break;
            case android.R.id.home:
                mCurrentTag = HOST_TAG;
                updateActionBar();
                getSupportFragmentManager().popBackStack();
                break;
        }
        return true;
    }

    // 顶部状态栏的刷新操作
    private void refresh() {
        ((BaseListFragment) getSupportFragmentManager().findFragmentByTag(HOST_TAG)).refresh();
    }

    private void browseProfile() {
        Toast.makeText(MainActivity.this, "查看我的共享文件", Toast.LENGTH_LONG).show();
    }

    private void browseDownloadHistory() {
        Toast.makeText(MainActivity.this, "查看下载历史", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHostItemClick(Host host) {
        FragmentTransaction bt = getSupportFragmentManager().beginTransaction();
        bt.addToBackStack(HOST_TAG);
        bt.replace(R.id.fl_container, new ShareFileListFragment(), SHARE_FILE_TAG);
        bt.commit();

        mCurrentTag = SHARE_FILE_TAG;
        updateActionBar();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentTag.equals(SHARE_FILE_TAG)) {
            mCurrentTag = HOST_TAG;
            updateActionBar();
        } else if (mCurrentTag.equals(HOST_TAG)) {
            long currTime = System.currentTimeMillis();
            if (currTime - sLastBackPressTime > 1500) {
                sLastBackPressTime = currTime;
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onBackPressed();
    }

    private void updateActionBar() {
        String title;
        switch (mCurrentTag) {
            case HOST_TAG:
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(false);
                }
                title = ACTIONBAR_TITLE_HOST_LIST;
                break;
            case SHARE_FILE_TAG:
                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(true);
                }
                title = ACTIONBAR_TITLE_FILE_LIST;
                break;
            default:
                title = ACTIONBAR_TITLE_DEFAULT;
        }
        mToolbar.setTitle(title);
    }
}
