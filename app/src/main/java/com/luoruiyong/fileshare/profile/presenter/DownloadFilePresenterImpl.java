package com.luoruiyong.fileshare.profile.presenter;

import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.profile.contract.DownloadFileContract;
import com.luoruiyong.fileshare.profile.model.DownloadFileModel;

import java.util.List;

public class DownloadFilePresenterImpl implements DownloadFileContract.Presenter, DownloadFileModel.OnScanDownloadFileFinishListener {

    private DownloadFileContract.View mView;
    private DownloadFileModel mModel;

    public DownloadFilePresenterImpl(DownloadFileContract.View view) {
        this.mView = view;
        this.mModel = new DownloadFileModel();
        this.mModel.setOnScanDownloadFileFinishListener(this);
    }

    @Override
    public void deleteFile(String file) {

    }

    @Override
    public void getAllDownloadFiles() {
        mModel.startScanDownloadFiles();
    }

    @Override
    public void onScanDownloadFileFinish(List<ShareFile> list) {
        mView.updateDownloadFileList(list);
    }
}
