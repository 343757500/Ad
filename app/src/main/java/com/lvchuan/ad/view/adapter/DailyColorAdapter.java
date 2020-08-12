package com.lvchuan.ad.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.lvchuan.ad.view.Holder.BaseHolderRV;
import com.lvchuan.ad.view.Holder.DailyColorHolder;
import com.lvchuan.ad.view.Holder.DailyRecoveryHolder;

import java.util.List;

public class DailyColorAdapter extends BaseAdapterRV {
  public DailyColorAdapter(Context context, List listData) {
    super(context, listData);
  }

  @Override
  public BaseHolderRV createViewHolder(Context context, ViewGroup parent, int viewType) {
    return new DailyColorHolder(context,parent,this,viewType);
  }
}
