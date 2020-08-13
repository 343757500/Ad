package com.lvchuan.ad.view.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.just.agentweb.AgentWeb;
import com.lvchuan.ad.LocalVImageHolderView;
import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseFragment;
import com.lvchuan.ad.bean.AdEntity;
import com.lvchuan.ad.bean.InitBean;
import com.lvchuan.ad.bean.NettyCmdBean;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

public class H5ViewFragment extends BaseFragment {

    private LinearLayout lin_web;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_h5view;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        lin_web = findView(R.id.lin_web);

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



    //接收activity传过来的数据展示广播
    @Subscriber(tag = "initBean")
    private void initBean(InitBean initBean) {
        if ("3".equals(initBean.getReturn_info().get(0).getMode())) {
            lin_web = findView(R.id.lin_web);
            List<InitBean.ReturnInfoBean.AdvertisementBean> advertisement = initBean.getReturn_info().get(0).getAdvertisement();
            AgentWeb agentWeb = AgentWeb.with(this.getActivity())
                    .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()//进度条
                    .createAgentWeb()
                    .ready()
                    .go(advertisement.get(0).getPath());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
