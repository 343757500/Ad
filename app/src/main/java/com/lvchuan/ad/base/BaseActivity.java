package com.lvchuan.ad.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lvchuan.ad.utils.Utils;


/**
 * Activity基类，所有的Activity都需要继承此类。
 * 封装： 查看子控件，设置监听器，初始化数据，
 * toast, showDialog, showProgressDialog等方法
 *
 * @author WJQ
 */
public abstract class BaseActivity extends AppCompatActivity
		implements IUIOperation{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(getLayoutRes());
		
		// 系统的一个根布局，可以查找到activity布局的所有的子控件
		View root = findViewById(android.R.id.content);

		// 查找activity布局中所有的Button（ImageButton），并设置点击事件
		Utils.findButtonAndSetOnClickListener(root, this);

		// 状态栏透明
		Global.setNoStatusBarFullMode(this);
		//谷歌状态栏文字底色的黑白   true就是黑色底，false为白色底
		Global.setAndroidNativeLightStatusBar(this,false);

		initView();
		initListener();
		initData();
	}
	
	/** 查找子控件，可以省略强转 */
	public <T> T findView(int id) {
		@SuppressWarnings("unchecked")
		T view = (T) findViewById(id);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			default:
				onClick(v, v.getId());
				break;
		}
	}
	
	public void showToast(String text) {
		Global.showToast(text);
	}

	private ProgressDialog mPDialog;

	/**
	 * 显示加载提示框(不能在子线程调用)
	 */
	public void showProgressDialog(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mPDialog = new ProgressDialog(BaseActivity.this);
				mPDialog.setMessage(message);
				// 点击外部时不销毁
				mPDialog.setCanceledOnTouchOutside(false);

				// activity如果正在销毁或已销毁，不能show Dialog，否则出错。
				if (!isFinishing())
					mPDialog.show();
			}
		});
	}


	/**
	 * 销毁加载提示框
	 */
	public void dismissProgressDialog() {
		if (mPDialog != null) {
			mPDialog.dismiss();
			mPDialog = null;
		}
	}

	/**
	 * 显示对话框
	 *
	 * @param title 标题
	 * @param message 内容
	 * @param listener 回调监听器
	 */
	public void showDialog(String title, String message,
                           final OnDialogClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (listener != null) {
							listener.onConfirm(dialog);
						}
					}
				});
		builder.setNegativeButton("取消", null);
		if (!isFinishing())
			builder.create().show();
	}

	/** 提示对话框*/
	public void showDialog(String title, String message) {
		showDialog(title, message, null);
	}

	/** 提示对话框*/
	public void showDialog(String message) {
		showDialog("", message, null);
	}

    /** 对话框点击回调 */
	public interface OnDialogClickListener {

		/** 确定 */
		public void onConfirm(DialogInterface dialog);

		/** 取消 */
		public void onCancel(DialogInterface dialog) ;
	}

}



















