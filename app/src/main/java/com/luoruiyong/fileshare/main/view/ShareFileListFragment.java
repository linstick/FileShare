package com.luoruiyong.fileshare.main.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.luoruiyong.fileshare.base.BaseListFragment;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.main.adapter.ShareFileListAdapter;
import com.luoruiyong.fileshare.main.contract.ShareFileContract;
import com.luoruiyong.fileshare.main.presenter.ShareFilePresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class ShareFileListFragment extends BaseListFragment implements ShareFileContract.View, ShareFileListAdapter.OnPartialItemClickListener {
    private static final String TAG = "ShareFileListFragment";

    private static final String HOST_TAG = "host_tag";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoItemLayout;
    private TextView mNoItemMessageTv;
    private Button mRefreshBtn;

    private List<ShareFile> mList;
    private ShareFileListAdapter mAdapter;
    private Host mHost;

    private ShareFileContract.Presenter mPresenter;
    private OnBackToHostFragmentListener mListener;

    public static ShareFileListFragment newInstance(Host host) {
        ShareFileListFragment fragment = new ShareFileListFragment();
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
        mPresenter = new ShareFilePresenterImpl(this);
        mList = new ArrayList<>();
        mAdapter = new ShareFileListAdapter(mList);
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
            mAdapter.setOnParticalItemClickListener(this);
            mListener = (OnBackToHostFragmentListener) getActivity();
        }

        refresh();
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

    @Override
    public void refresh() {
        mNoItemLayout.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(true);
        mList.clear();
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
    public void onDownloadViewClick(int position) {
        mPresenter.downloadFile();
    }

    // -------------------------------------


    public interface OnBackToHostFragmentListener {
        void onBackToHostFragment();
    }
}
