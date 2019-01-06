package com.luoruiyong.fileshare.profile.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.luoruiyong.fileshare.model.FileChooseUtil;
import com.luoruiyong.fileshare.model.FileUtil;
import com.luoruiyong.fileshare.profile.adapter.MyShareFileListAdapter;
import com.luoruiyong.fileshare.profile.contract.MyShareFileContract;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MyShareFileFragment extends Fragment implements
        MyShareFileContract.View,
        BaseFileListAdapter.OnPartialItemClickListener {
    private static final String TAG = "MyShareFileFragment";
    private static final int CHOOSE_FILE_REQUEST_CODE = 1;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoItemLayout;
    private TextView mNoItemMessageTv;
    private Button mRefreshBtn;
    private FloatingActionButton mAddShareFileBtn;

    private List<ShareFile> mList;
    private MyShareFileListAdapter mAdapter;

//    private MyShareFileContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.rv_list);
        mNoItemLayout = view.findViewById(R.id.ll_no_item_layout);
        mNoItemMessageTv = view.findViewById(R.id.tv_message);
        mRefreshBtn = view.findViewById(R.id.btn_refresh);
        mAddShareFileBtn = view.findViewById(R.id.btn_add_share_file);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mPresenter = new MyShareFilePresenterImpl(this);
        mList = DataSource.getMySharedFileList();
        mAdapter = new MyShareFileListAdapter(mList);
        mAdapter.setOnPartialItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });

//        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                refresh();
//            }
//        });

        mRefreshLayout.setEnabled(false);
        mNoItemMessageTv.setText("您还没有共享文件哦~");
        mRefreshBtn.setVisibility(View.GONE);
        if (mList == null || mList.size() == 0) {
            mNoItemLayout.setVisibility(View.VISIBLE);
        } else {
            mNoItemLayout.setVisibility(View.GONE);
        }

        mAddShareFileBtn.setVisibility(View.VISIBLE);
        mAddShareFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                getActivity().startActivityForResult(intent, CHOOSE_FILE_REQUEST_CODE);
            }
        });

//        refresh();
    }

    // -------Presenter触发的UI事件------------
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

//    public void refresh() {
//        mNoItemLayout.setVisibility(View.GONE);
//        mRefreshLayout.setRefreshing(true);
//        mList.clear();
//        mAdapter.notifyDataSetChanged();
//        mPresenter.getAllShareFiles();
//    }

    // ---------列表项事件回调-------------------
    @Override
    public void onBackViewClick() {
        // 此页面在列表中不会出现不处理返回按钮，此事件不会触发
    }

    @Override
    public void onOperateViewClick(final int position) {
        // 此页面中是移除操作
        new AlertDialog.Builder(getContext())
                .setTitle("提示")
                .setMessage("您确定要移除“" + mList.get(position).getName() +"”共享文件吗？这次移除并不会删除本地文件。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataSource.removeMySharedFile(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "已移除 ", Toast.LENGTH_SHORT).show();
                        if (mList.size() == 0) {
                            mNoItemLayout.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
//        mPresenter.removeShareFile(mList.get(position));
    }

    // -------------------------------------


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            String url = FileChooseUtil.getChooseFileResultPath(getContext(), data.getData());
            Log.d(TAG, "onActivityResult: choose file url = " + url);
            if (url != null) {
                ShareFile shareFile = FileUtil.localFileToShareFile(url);
                DataSource.addMySharedFile(shareFile);
                mAdapter.notifyDataSetChanged();
                if (mNoItemLayout.getVisibility() == View.VISIBLE) {
                    mNoItemLayout.setVisibility(View.GONE);
                }
                Toast.makeText(getContext(), "共享成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
