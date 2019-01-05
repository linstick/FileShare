package com.luoruiyong.fileshare.main.presenter;

import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.main.contract.HostContract;
import com.luoruiyong.fileshare.main.model.HostListModel;

public class HostPresenterImpl implements HostContract.Presenter, HostListModel.OnHostDataReceiveListener {

    private HostListModel mModel;
    private HostContract.View mView;

    public HostPresenterImpl(HostContract.View view) {
        this.mModel = new HostListModel();
        this.mModel.setOnHostDataReceiveListener(this);
        this.mView = view;
    }

    @Override
    public void refresh() {
        mModel.sendShareHostRequest();
    }


    @Override
    public void onHostDataReceive(Host host) {
        mView.appendHostList(host);
    }
}
