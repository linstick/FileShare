package com.luoruiyong.fileshare.profile.presenter;

import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.profile.contract.MyShareFileContract;
import com.luoruiyong.fileshare.profile.model.MyShareFileModel;

import java.util.List;

public class MyShareFilePresenterImpl implements MyShareFileContract.Presenter, MyShareFileModel.OnScanMyShareFileFinishListener {

    private MyShareFileContract.View mView;
    private MyShareFileModel mModel;

    public MyShareFilePresenterImpl(MyShareFileContract.View view) {
        this.mView = view;
        this.mModel = new MyShareFileModel();
        this.mModel.setOnScanMyShareFileFinishListener(this);
    }

    @Override
    public void addShareFile(String url) {

    }

    @Override
    public void removeShareFile(ShareFile file) {

    }

    @Override
    public void getAllShareFiles() {
        this.mModel.startScanShareFiles();
    }

    @Override
    public void onScanMyShareFileFinish(List<ShareFile> list) {
        this.mView.updateShareFileList(list);
    }
}
