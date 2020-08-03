package com.lvchuan.ad.base;

import android.os.Bundle;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lvchuan.ad.utils.Utils;


/**
 * Fragment基类，所有的Fragment都需要继承此类。
 * 封装查看子控件，设置监听器，初始化数据
 *
 * @author WJQ
 */
public abstract class BaseFragment extends Fragment
		implements IUIOperation {

	/** 管理Fragment的Activity */
	public BaseActivity mActivity;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化Activity对象
		mActivity = (BaseActivity)getActivity();
	}

	/** Fragment显示的布局 */
	public View mRoot;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
		if (mRoot == null) {
			mRoot = inflater.inflate(getLayoutRes(), null);

			// 查找布局中的所有的button(ImageButton),并设置点击事件
			Utils.findButtonAndSetOnClickListener(mRoot, this);
			
			initView();
			initListener();
			initData();
			
		} else {
			// 解除mRoot与其父控件的关系
			unbindWidthParent(mRoot);
		}
		
		return mRoot;
	}


	/**
	 * 解除父子控件关系
	 * 
	 * @param view
	 */
	public void unbindWidthParent(View view) {
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
	}

	/** 查找子控件，可以省略强转 */
	public <T> T findView(int id) {
		@SuppressWarnings("unchecked")
		T view = (T) mRoot.findViewById(id);
		return view;
	}

	
	@Override
	public void onClick(View v) {
		onClick(v, v.getId());
	}
	
	public void showToast(String text) {
		Global.showToast(text);
	}

}

