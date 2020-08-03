package com.lvchuan.ad.view.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author WJQ
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public int getItemPosition(Object object) {
        //注意：默认是PagerAdapter.POSITION_UNCHANGED，不会重新加载
            return POSITION_NONE;

    }

}
