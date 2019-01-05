package com.luoruiyong.fileshare.profile.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseFileListAdapter;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.List;

public class DownloadFileListAdapter extends BaseFileListAdapter {


    public DownloadFileListAdapter(List<ShareFile> mList) {
        super(mList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).mOperateIv.setImageResource(R.drawable.ic_delete_gray);
        }
    }
}
