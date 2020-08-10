package com.lvchuan.ad.view.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseFragment;
import com.lvchuan.ad.bean.NettyCmdBean;

import org.simple.eventbus.Subscriber;

public class H5ViewFragment extends BaseFragment {

    private LinearLayout lin_web;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_h5view;
    }

    @Override
    public void initView() {
        lin_web = findView(R.id.lin_web);
        AgentWeb.with(this)
                .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                .createAgentWeb()
                .ready()
                .go("http://120.78.175.246/apps/#/?boxCode=LZ02-584300");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v, int id) {

    }


    //netty接收到广告更换的广播
    @Subscriber(tag = "nettyCmdBean")
    private void nettyCmdBean(NettyCmdBean nettyCmdBean) {

    }
}
