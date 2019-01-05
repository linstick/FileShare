package com.luoruiyong.fileshare.main.contract;

import com.luoruiyong.fileshare.base.BasePresenter;
import com.luoruiyong.fileshare.base.BaseView;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.List;

public interface ShareFileContract {

    interface Presenter extends BasePresenter {
        void refresh(Host host);

        void downloadFile();
    }

    interface View extends BaseView<Presenter> {
        void hideRefreshLayout();

        void appendShareFileList(ShareFile shareFile);

        void updateShareFileList(List<ShareFile> list);
    }
}
