package com.luoruiyong.fileshare.profile.contract;

import com.luoruiyong.fileshare.base.BasePresenter;
import com.luoruiyong.fileshare.base.BaseView;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.List;

public interface DownloadFileContract {

    interface Presenter extends BasePresenter {
        // 对View提供的方法
        void deleteFile(String file);
        void getAllDownloadFiles();
    }

    interface View extends BaseView<Presenter> {
        // 对Presenter提供的方法
        void updateDownloadFileList(List<ShareFile> list);
    }

}
