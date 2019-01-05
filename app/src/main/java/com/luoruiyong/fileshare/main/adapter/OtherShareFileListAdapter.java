package com.luoruiyong.fileshare.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.fileshare.R;
import com.luoruiyong.fileshare.base.BaseFileListAdapter;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.utils.FileSizeFommatUtil;

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
            ((ItemViewHolder)holder).mOperateIv.setImageResource(shareFile.isDownload() ? R.drawable.ic_finish_gray : R.drawable.ic_file_download_gray);
        }
    }
}
