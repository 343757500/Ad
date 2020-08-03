package com.lvchuan.ad.view.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvchuan.ad.R;
import com.lvchuan.ad.view.adapter.BaseAdapterRV;

public class DailyRecoveryNullHolder extends BaseHolderRV {

    private TextView tv_category;

    public DailyRecoveryNullHolder(Context context, ViewGroup parent, BaseAdapterRV adapter, int itemType) {
        super(context, parent, adapter, itemType, R.layout.item_daily_null);
    }

    @Override
    public void onFindViews(View itemView) {

        tv_category = itemView.findViewById(R.id.tv_category);
    }

    @Override
    protected void onRefreshView(Object bean, int position) {
        if (position%2 == 1) {
            itemView.setBackgroundColor(Color.parseColor("#123F8D"));
        }else{
            itemView.setBackgroundColor(Color.parseColor("#183E88"));
        }
        String name = (String) bean;
        tv_category.setText(name);

    }
}
