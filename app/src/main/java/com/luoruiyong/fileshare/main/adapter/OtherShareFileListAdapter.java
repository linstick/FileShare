package com.luoruiyong.fileshare.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseFileListAdapter;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.List;

public class OtherShareFileListAdapter extends BaseFileListAdapter {


    public OtherShareFileListAdapter(List<ShareFile> mList) {
        super(mList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
            ShareFile shareFile = mList.get(mHost == null ? position : position - 1);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            switch (shareFile.getStatus()) {
                case ShareFile.STATUS_DOWNLOADED:
                    viewHolder.mOperateIv.setImageResource(R.drawable.ic_finish_gray);
                    viewHolder.mOperateIv.setVisibility(View.VISIBLE);
                    viewHolder.mDownloadingMsgTv.setVisibility(View.GONE);
                    break;
                case ShareFile.STATUS_SHARED:
                    viewHolder.mOperateIv.setImageResource(R.drawable.ic_file_download_gray);
                    viewHolder.mOperateIv.setVisibility(View.VISIBLE);
                    viewHolder.mDownloadingMsgTv.setVisibility(View.GONE);
                    break;
                case ShareFile.STATUS_DOWNLOADING:
                    viewHolder.mOperateIv.setVisibility(View.GONE);
                    viewHolder.mDownloadingMsgTv.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
