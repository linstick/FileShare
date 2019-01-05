package com.luoruiyong.fileshare.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.bean.Host;

import java.util.List;

public class HostListAdapter extends RecyclerView.Adapter<HostListAdapter.ViewHolder> {

    private List<Host> mList;
    private OnItemClickListener mListener;

    public HostListAdapter(List<Host> list) {
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Host host = mList.get(position);
        holder.mHostNameTv.setText(host.getName());
        holder.mIpAddressTv.setText(host.getIpAddress());
        holder.mFileCountTv.setText(host.getFileCount() + "");
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mItemLayout;
        TextView mHostNameTv;
        TextView mIpAddressTv;
        TextView mFileCountTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemLayout = itemView.findViewById(R.id.rl_item_layout);
            mHostNameTv = itemView.findViewById(R.id.tv_host_name);
            mIpAddressTv = itemView.findViewById(R.id.tv_ip_address);
            mFileCountTv = itemView.findViewById(R.id.tv_count);
        }
    }
}
