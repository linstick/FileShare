package com.luoruiyong.fileshare.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.main.view.HostListFragment;
import com.luoruiyong.fileshare.main.view.ShareFileListFragment;

public class MainActivity extends AppCompatActivity implements
        HostListFragment.OnHostItemClickListener, ShareFileListFragment.OnBackToHostFragmentListener{

    private static final String TAG = "MainActivity";
    private static long sLastBackPressTime = 0;
    private final String HOST_TAG = "host";
    private final String SHARE_FILE_TAG = "share_file";
    private final String ACTIONBAR_TITLE_DEFAULT = "共享小助手";
    private final String ACTIONBAR_TITLE_HOST_LIST = "共享主机";
    private final String ACTIONBAR_TITLE_FILE_LIST = "共享文件";
    private Toolbar mToolbar;
    private String mCurrentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCurrentTag = HOST_TAG;
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new HostListFragment(), HOST_TAG).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateActionBarTitle();
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
        }
        return true;
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
        bt.replace(R.id.fl_container, ShareFileListFragment.newInstance(host), SHARE_FILE_TAG);
        bt.commit();

        mCurrentTag = SHARE_FILE_TAG;
        updateActionBarTitle();
    }

    @Override
    public void onBackToHostFragment() {
        mCurrentTag = HOST_TAG;
        updateActionBarTitle();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentTag.equals(SHARE_FILE_TAG)) {
            mCurrentTag = HOST_TAG;
            updateActionBarTitle();
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

    private void updateActionBarTitle() {
        String title;
        switch (mCurrentTag) {
            case HOST_TAG:
                title = ACTIONBAR_TITLE_HOST_LIST;
                break;
            case SHARE_FILE_TAG:
                title = ACTIONBAR_TITLE_FILE_LIST;
                break;
            default:
                title = ACTIONBAR_TITLE_DEFAULT;
        }
        mToolbar.setTitle(title);
    }
}
