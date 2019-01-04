package com.luoruiyong.fileshare.main.presenter;

import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.main.contract.ShareFileContract;
import com.luoruiyong.fileshare.main.model.ShareFileModel;

import java.util.List;

public class ShareFilePresenterImpl implements ShareFileContract.Presenter, ShareFileModel.OnShareFileDataReceiveListener {

    private ShareFileContract.View mView;
    private ShareFileModel mModel;

    public ShareFilePresenterImpl(ShareFileContract.View view) {
        this.mView = view;
        this.mModel = new ShareFileModel();
        this.mModel.setOnShareFileDataReceiveListener(this);
    }

    @Override
    public void refresh(Host host) {
        mModel.sentShareFileRequest(host);
    }

    @Override
    public void downloadFile() {

    }

    @Override
    public void onReceiveFileList(List<ShareFile> list) {
        mView.updateShareFileList(list);
    }
}
