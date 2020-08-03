package com.lvchuan.ad.view.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchuan.ad.R;
import com.lvchuan.ad.view.adapter.BaseAdapterRV;
import com.squareup.picasso.Picasso;

public class DailyRecoveryHolder extends BaseHolderRV {

    private TextView tv_category;
    private ImageView iv_p;

    public DailyRecoveryHolder(Context context, ViewGroup parent, BaseAdapterRV adapter, int itemType) {
        super(context, parent, adapter, itemType, R.layout.item_daily);
    }

    @Override
    public void onFindViews(View itemView) {

        tv_category = itemView.findViewById(R.id.tv_category);
        iv_p = itemView.findViewById(R.id.iv_p);
    }

    @Override
    protected void onRefreshView(Object bean, int position) {
       /* if (position%2 == 1) {
            itemView.setBackgroundColor(Color.parseColor("#123F8D"));
        }else{
            itemView.setBackgroundColor(Color.parseColor("#183E88"));
        }*/


      switch (position){
          case 0:
              Picasso.with(context).load(R.mipmap.p1).into(iv_p);
              break;
          case 1:
              Picasso.with(context).load(R.mipmap.p2).into(iv_p);
              break;
          case 2:
              Picasso.with(context).load(R.mipmap.p3).into(iv_p);
              break;
          case 3:
              Picasso.with(context).load(R.mipmap.p4).into(iv_p);
              break;
      }
        String name = (String) bean;
        tv_category.setText(name);

    }
}
