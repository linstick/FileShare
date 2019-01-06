package com.luoruiyong.fileshare.main.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseActivity;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.eventbus.DataChangeEvent;
import com.luoruiyong.fileshare.main.adapter.OtherShareFileListAdapter;
import com.luoruiyong.fileshare.main.contract.ShareFileContract;
import com.luoruiyong.fileshare.main.presenter.OtherShareFilePresenterImpl;
import com.luoruiyong.fileshare.model.DataSource;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OtherShareFileListFragment extends Fragment implements ShareFileContract.View, OtherShareFileListAdapter.OnPartialItemClickListener {
    private static final String TAG = "OtherShareFileListFragment";

    private static final String HOST_TAG = "host_tag";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoItemLayout;
    private TextView mNoItemMessageTv;
    private Button mRefreshBtn;

    private List<ShareFile> mList;
    private OtherShareFileListAdapter mAdapter;
    private Host mHost;

    private ShareFileContract.Presenter mPresenter;
    private OnBackToHostFragmentListener mListener;

    public static OtherShareFileListFragment newInstance(Host host) {
        OtherShareFileListFragment fragment = new OtherShareFileListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HOST_TAG, host);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.rv_list);
        mNoItemLayout = view.findViewById(R.id.ll_no_item_layout);
        mNoItemMessageTv = view.findViewById(R.id.tv_message);
        mRefreshBtn = view.findViewById(R.id.btn_refresh);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHost = (Host) getArguments().getSerializable(HOST_TAG);
        mPresenter = new OtherShareFilePresenterImpl(this);
        mList = DataSource.getOtherSharedFileList();
        mAdapter = new OtherShareFileListAdapter(mList);
        mAdapter.setHost(mHost);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        mNoItemMessageTv.setText("获取当前主机的共享文件失败");
        mRefreshBtn.setText("刷新");

        if (getActivity() instanceof OnBackToHostFragmentListener) {
            mAdapter.setOnPartialItemClickListener(this);
            mListener = (OnBackToHostFragmentListener) getActivity();
        }

        refresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataChangeEvent(DataChangeEvent event) {
        switch (event) {
            case OTHER_SHARE_FILE_CHANGE:
                updateUi();
                break;
        }
    }

    private void updateUi() {
        mHost.setFileCount(mList.size());
        if (mList.size() == 0) {
            mNoItemLayout.setVisibility(View.VISIBLE);
        } else {
            mNoItemLayout.setVisibility(View.GONE);
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mAdapter.notifyDataSetChanged();

    }

    // -------Presenter触发的UI事件------------

    @Override
    public void hideRefreshLayout() {
        if (mRefreshLayout.isRefreshing()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                    if (mList == null || mList.size() == 0) {
                        mNoItemLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void appendShareFileList(final ShareFile shareFile) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                mList.add(shareFile);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void updateShareFileList(final List<ShareFile> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                if (list == null || list.size() == 0) {
                    mNoItemLayout.setVisibility(View.VISIBLE);
                } else {
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // -----------------------------------------

    public void refresh() {
        mNoItemLayout.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(true);
        DataSource.clearOtherShareFileList();
        mAdapter.notifyDataSetChanged();
        mPresenter.refresh(mHost);
    }

    // 列表点击事件回调

    @Override
    public void onBackViewClick() {
        if (mListener != null) {
            mListener.onBackToHostFragment();
        }
    }

    @Override
    public void onOperateViewClick(final int position) {
        final ShareFile shareFile = mList.get(position);
        if (shareFile.getStatus() == ShareFile.STATUS_SHARED) {
            // 下载之前需要检查权限
            BaseActivity activity = (BaseActivity) getActivity();
            if (!activity.isPermissionGranted()) {
                activity.requestPermissions(new BaseActivity.OnRequestPermissionsResultCallBack() {
                    @Override
                    public void onGranted() {
                        download(position);
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(getContext(), "您拒绝了应用获取设备的文件读写权限，无法正常下载文件", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                download(position);
            }
        }
    }

    // -------------------------------------


    private void download(int position) {
        // 已经获取到权限，开始执行下载操作
        ShareFile shareFile = mList.get(position);
        shareFile.setStatus(ShareFile.STATUS_DOWNLOADING);
        mAdapter.notifyItemChanged(mHost == null ? position : position + 1);
        Toast.makeText(getContext(), "开始下载文件：" + shareFile.getName(), Toast.LENGTH_SHORT).show();
        mPresenter.downloadFile();
    }

    public interface OnBackToHostFragmentListener {
        void onBackToHostFragment();
    }
}
