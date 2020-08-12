package com.lvchuan.ad.view.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchuan.ad.R;
import com.lvchuan.ad.bean.StatisticsBean;
import com.lvchuan.ad.view.adapter.BaseAdapterRV;
import com.squareup.picasso.Picasso;

public class DailyRecoveryHolder extends BaseHolderRV {

  private TextView tv_category;
  private ImageView iv_p;
  private TextView tv_unit;

  public DailyRecoveryHolder(Context context, ViewGroup parent, BaseAdapterRV adapter, int itemType) {
    super(context, parent, adapter, itemType, R.layout.item_daily);
  }

  @Override
  public void onFindViews(View itemView) {

    tv_category = itemView.findViewById(R.id.tv_category);
    iv_p = itemView.findViewById(R.id.iv_p);
    tv_unit = itemView.findViewById(R.id.tv_unit);
  }

  @Override
  protected void onRefreshView(Object bean, int position) {
       /* if (position%2 == 1) {
            itemView.setBackgroundColor(Color.parseColor("#123F8D"));
        }else{
            itemView.setBackgroundColor(Color.parseColor("#183E88"));
        }*/

    StatisticsBean.ReturnInfoBean.DataBean dataBean = (StatisticsBean.ReturnInfoBean.DataBean) bean;
    switch (dataBean.getRecycleName()){
      case "纸类":
        Picasso.with(context).load(R.mipmap.p1).into(iv_p);
        break;
      case "织物类":
      case  "废纺":
        Picasso.with(context).load(R.mipmap.p2).into(iv_p);
        break;
      case "金属/塑料":
        Picasso.with(context).load(R.mipmap.p3).into(iv_p);
        break;
      case "饮料瓶":
        Picasso.with(context).load(R.mipmap.p4).into(iv_p);
        break;
    }

    tv_category.setText(dataBean.getRecycleName());
    tv_unit.setText(dataBean.getRecycleWeight()+dataBean.getUnit());

  }
}
