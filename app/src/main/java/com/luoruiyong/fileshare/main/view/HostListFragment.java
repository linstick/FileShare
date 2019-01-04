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
import android.widget.Toast;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseListFragment;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.main.adapter.HostListAdapter;
import com.luoruiyong.fileshare.main.adapter.OnItemClickListener;
import com.luoruiyong.fileshare.main.contract.HostContract;
import com.luoruiyong.fileshare.main.presenter.HostPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class HostListFragment extends BaseListFragment implements HostContract.View, OnItemClickListener {
    private static final String TAG = "HostListFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Host> mList;
    private HostListAdapter mAdapter;

    private HostContract.Presenter mPresenter;
    private boolean mRefreshing = false;
    private OnHostItemClickListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.rv_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = new HostPresenterImpl(this);
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mAdapter = new HostListAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        if (getActivity() instanceof OnHostItemClickListener) {
            mListener = (OnHostItemClickListener) getActivity();
        }
    }

    // -------Presenter触发的UI事件------------
    @Override
    public void appendHostList(final Host host) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRefreshing) {
                    mRefreshLayout.setRefreshing(false);
                    mRefreshing = false;
                }
                mList.add(host);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void hideRefreshLayout() {
        if (mRefreshing) {
            mRefreshing = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // -----------------------------------------


    // --------MainActivity触发的事件------------
    @Override
    public void refresh() {
        if (!mRefreshing) {
            mRefreshing = true;
            mRefreshLayout.setRefreshing(true);
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mPresenter.refresh();
        }
    }
    // -----------------------------------------

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
