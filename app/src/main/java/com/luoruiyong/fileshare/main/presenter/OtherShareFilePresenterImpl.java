package com.luoruiyong.fileshare.main.presenter;

import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.main.contract.ShareFileContract;
import com.luoruiyong.fileshare.main.model.OtherShareFileModel;

import java.util.List;

public class OtherShareFilePresenterImpl implements ShareFileContract.Presenter, OtherShareFileModel.OnShareFileDataReceiveListener {

    private ShareFileContract.View mView;
    private OtherShareFileModel mModel;

    public OtherShareFilePresenterImpl(ShareFileContract.View view) {
        this.mView = view;
        this.mModel = new OtherShareFileModel();
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
