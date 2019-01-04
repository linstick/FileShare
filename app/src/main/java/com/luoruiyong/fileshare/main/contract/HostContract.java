package com.luoruiyong.fileshare.main.contract;

import com.luoruiyong.fileshare.base.BasePresenter;
import com.luoruiyong.fileshare.base.BaseView;
import com.luoruiyong.fileshare.bean.Host;

public interface HostContract {

    interface Presenter extends BasePresenter {
        void refresh();
    }

    interface View extends BaseView<Presenter> {
        void appendHostList(Host host);

        void hideRefreshLayout();
    }
}
