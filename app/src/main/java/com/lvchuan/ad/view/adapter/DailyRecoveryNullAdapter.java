package com.lvchuan.ad.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.lvchuan.ad.view.Holder.BaseHolderRV;
import com.lvchuan.ad.view.Holder.DailyRecoveryHolder;
import com.lvchuan.ad.view.Holder.DailyRecoveryNullHolder;

import java.util.List;

public class DailyRecoveryNullAdapter extends BaseAdapterRV {
    public DailyRecoveryNullAdapter(Context context, List listData) {
        super(context, listData);
    }

    @Override
    public BaseHolderRV createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new DailyRecoveryNullHolder(context,parent,this,viewType);
    }
}
