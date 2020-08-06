package com.lvchuan.ad.view.Holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvchuan.ad.R;
import com.lvchuan.ad.bean.StatisticsBean;
import com.lvchuan.ad.view.adapter.BaseAdapterRV;
import com.squareup.picasso.Picasso;

public class DailyColorHolder extends BaseHolderRV {

    private RelativeLayout tv_category;
    private TextView tv_unit;
    private int [] colorList ={R.color.bg1,R.color.bg2,R.color.bg2,R.color.bg3};

    public DailyColorHolder(Context context, ViewGroup parent, BaseAdapterRV adapter, int itemType) {
        super(context, parent, adapter, itemType, R.layout.item_color);
    }

    @Override
    public void onFindViews(View itemView) {

        tv_category = itemView.findViewById(R.id.tv_category);
        tv_unit = itemView.findViewById(R.id.tv_unit);
    }

    @Override
    protected void onRefreshView(Object bean, int position) {
        StatisticsBean.ReturnInfoBean.DataBean dataBean = (StatisticsBean.ReturnInfoBean.DataBean) bean;

        tv_category.setBackgroundColor(colorList[position]);
        tv_unit.setText(dataBean.getRecycleName());

    }
}
