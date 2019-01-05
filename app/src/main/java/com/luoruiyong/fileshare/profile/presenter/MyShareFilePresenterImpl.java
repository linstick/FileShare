package com.luoruiyong.fileshare.profile.presenter;

import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.profile.contract.MyShareFileContract;
import com.luoruiyong.fileshare.profile.model.MyShareFileModel;

public class MyShareFilePresenterImpl implements MyShareFileContract.Presenter {

    private MyShareFileContract.View mView;
    private MyShareFileModel mModel;

    public MyShareFilePresenterImpl(MyShareFileContract.View view) {
        this.mView = view;
        this.mModel = new MyShareFileModel();
    }

    @Override
    public void addShareFile() {

    }

    @Override
    public void deleteShareFile(ShareFile file) {

    }

    @Override
    public void getAllShareFiles() {

    }
}
