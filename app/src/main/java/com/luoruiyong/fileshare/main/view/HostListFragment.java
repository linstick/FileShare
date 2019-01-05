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
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.main.adapter.HostListAdapter;
import com.luoruiyong.fileshare.main.contract.HostContract;
import com.luoruiyong.fileshare.main.presenter.HostPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class HostListFragment extends Fragment implements HostContract.View, HostListAdapter.OnItemClickListener {
    private static final String TAG = "HostListFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoItemLayout;
    private TextView mNoItemMessageTv;
    private Button mRefreshBtn;

    private List<Host> mList;
    private HostListAdapter mAdapter;

    private HostContract.Presenter mPresenter;
    private OnHostItemClickListener mListener;

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

        mPresenter = new HostPresenterImpl(this);
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (mList.size() == 0) {
            mNoItemLayout.setVisibility(View.VISIBLE);
        } else {
            mNoItemLayout.setVisibility(View.GONE);
        }

        mAdapter = new HostListAdapter(mList);
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

        mNoItemMessageTv.setText("在当前局域网内暂时没有搜索到共享主机");
        mRefreshBtn.setText("重新搜索");

        if (getActivity() instanceof OnHostItemClickListener) {
            mAdapter.setOnItemClickListener(this);
            mListener = (OnHostItemClickListener) getActivity();
        }

        if (mList == null || mList.size() == 0) {
            refresh();
        }

    }

    // -------Presenter触发的UI事件------------
    @Override
    public void appendHostList(final Host host) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                mList.add(host);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

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
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // -----------------------------------------

    public void refresh() {
        mRefreshLayout.setRefreshing(true);
        mNoItemLayout.setVisibility(View.GONE);
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mPresenter.refresh();
    }

    // 列表点击事件回调
    @Override
    public void onItemClick(int position) {
        if (mListener != null) {
            Host host = mList.get(position);
            mListener.onHostItemClick(host);
        }

    }

    public interface OnHostItemClickListener {
        void onHostItemClick(Host host);
    }
}
