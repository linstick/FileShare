package com.luoruiyong.fileshare.profile.contract;

import com.luoruiyong.fileshare.base.BasePresenter;
import com.luoruiyong.fileshare.base.BaseView;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.List;

public interface MyShareFileContract {

    interface Presenter extends BasePresenter {
        void addShareFile(String url);
        void removeShareFile(ShareFile file);
        void getAllShareFiles();
    }

    interface View extends BaseView<Presenter> {
        void updateShareFileList(List<ShareFile> list);
    }

}
