package com.luoruiyong.fileshare.profile.presenter;

import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.profile.contract.DownloadFileContract;
import com.luoruiyong.fileshare.profile.model.DownloadFileModel;

public class DownloadFilePresenterImpl implements DownloadFileContract.Presenter {

    private DownloadFileContract.View mView;
    private DownloadFileModel mModel;

    public DownloadFilePresenterImpl(DownloadFileContract.View view) {
        this.mView = view;
        this.mModel = new DownloadFileModel();
    }

    @Override
    public void deleteFile(String file) {

    }

    @Override
    public void getAllDownloadFiles() {

    }
}
