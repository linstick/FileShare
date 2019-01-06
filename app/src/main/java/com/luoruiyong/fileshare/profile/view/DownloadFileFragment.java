package com.luoruiyong.fileshare.profile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.luoruiyong.fileshare.base.BaseFileListAdapter;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.model.DataSource;
import com.luoruiyong.fileshare.profile.adapter.DownloadFileListAdapter;
import com.luoruiyong.fileshare.profile.contract.DownloadFileContract;

import java.util.List;

public class DownloadFileFragment extends Fragment implements DownloadFileContract.View, BaseFileListAdapter.OnPartialItemClickListener {
    private static final String TAG = "DownloadFileFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoItemLayout;
    private TextView mNoItemMessageTv;
    private Button mRefreshBtn;

    private List<ShareFile> mList;
    private DownloadFileListAdapter mAdapter;

//    private DownloadFileContract.Presenter mPresenter;

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

//        mPresenter = new DownloadFilePresenterImpl(this);
        mList = DataSource.getDownloadShareFileList();
        mAdapter = new DownloadFileListAdapter(mList);
        mAdapter.setOnPartialItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });

        mRefreshLayout.setEnabled(false);
        mRefreshBtn.setVisibility(View.GONE);
//        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                refresh();
//            }
//        });

        mNoItemMessageTv.setText("下载记录空空如也~");
        if (mList == null || mList.size() == 0) {
            mNoItemLayout.setVisibility(View.VISIBLE);
        } else {
            mNoItemLayout.setVisibility(View.GONE);
        }
    }

    // -------Presenter触发的UI事件------------

    @Override
    public void updateDownloadFileList(final List<ShareFile> list) {
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

//    public void refresh() {
//        mNoItemLayout.setVisibility(View.GONE);
//        mRefreshLayout.setRefreshing(true);
//        mList.clear();
//        mAdapter.notifyDataSetChanged();
////        mPresenter.getAllDownloadFiles();
//    }

    // ---------列表项事件回调-------------------
    @Override
    public void onBackViewClick() {
        // 此页面在列表中不会出现不处理返回按钮，此事件不会触发
    }

    @Override
    public void onOperateViewClick(final int position) {
        // 此页面中是删除操作
        new AlertDialog.Builder(getContext())
                .setTitle("删除")
                .setMessage("您确定要永久删除“" + mList.get(position).getName() +"”文件吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataSource.deleteDownloadShareFile(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "删除成功 ", Toast.LENGTH_SHORT).show();
                        if (mList.size() == 0) {
                            mNoItemLayout.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();

//        mPresenter.deleteFile(mList.get(position).getUrl());
    }

    // -------------------------------------
}
