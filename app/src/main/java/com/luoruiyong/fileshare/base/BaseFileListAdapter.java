package com.luoruiyong.fileshare.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.utils.FileSizeFommatUtil;

import java.util.List;

public class BaseFileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<ShareFile> mList;
    protected Host mHost;
    protected OnPartialItemClickListener mListener;

    protected final int TYPE_HEADER = 0;
    protected final int TYPE_ITEM = 1;

    public BaseFileListAdapter(List<ShareFile> mList) {
        this.mList = mList;
    }

    public void setOnPartialItemClickListener(OnPartialItemClickListener listener) {
        this.mListener = listener;
    }

    public void setHost(Host host) {
        this.mHost = host;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HEADER:
                root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_file_header, parent, false);
                viewHolder = new HeaderViewHolder(root);
                break;
            case TYPE_ITEM:
                root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_file_list, parent, false);
                viewHolder = new ItemViewHolder(root);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            viewHolder.mBackIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onBackViewClick();
                    }
                }
            });
            viewHolder.mHostNameTv.setText(mHost.getName());
            viewHolder.mIpAddressTv.setText(mHost.getIpAddress());
            viewHolder.mFileCountTv.setText("共" + mHost.getFileCount() + "个文件");
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            final ShareFile shareFile = mList.get(mHost == null ? position : position - 1);
            viewHolder.mFileNameTv.setText(shareFile.getName());
            viewHolder.mFileSizeTv.setText(FileSizeFommatUtil.format(shareFile.getSize()));
            viewHolder.mOperateIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        mListener.onOperateViewClick(mHost == null ? position : position - 1);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        int headerCount = mHost == null ? 0 : 1;
        int itemCount = mList == null ? 0 : mList.size();
        return headerCount + itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHost != null) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mBackIv;
        TextView mHostNameTv;
        TextView mIpAddressTv;
        TextView mFileCountTv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mBackIv = itemView.findViewById(R.id.iv_back);
            mHostNameTv = itemView.findViewById(R.id.tv_host_name);
            mIpAddressTv = itemView.findViewById(R.id.tv_ip_address);
            mFileCountTv = itemView.findViewById(R.id.tv_count);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mItemLayout;
        TextView mFileNameTv;
        TextView mFileSizeTv;
        public ImageView mOperateIv;
        public TextView mDownloadingMsgTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItemLayout = itemView.findViewById(R.id.rl_item_layout);
            mFileNameTv = itemView.findViewById(R.id.tv_file_name);
            mFileSizeTv = itemView.findViewById(R.id.tv_file_size);
            mOperateIv = itemView.findViewById(R.id.iv_operate);
            mDownloadingMsgTv = itemView.findViewById(R.id.tv_downloading_msg);
        }
    }

    public interface OnPartialItemClickListener {
        void onBackViewClick();
        void onOperateViewClick(int position);
    }
}
