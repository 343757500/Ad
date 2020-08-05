package com.lvchuan.ad.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lvchuan.ad.Constants;
import com.lvchuan.ad.FileUtil;
import com.lvchuan.ad.R;

import java.io.File;

import static android.text.TextUtils.isEmpty;

public class ConfigInputFragment extends DialogFragment implements View.OnClickListener, View.OnTouchListener {


    static ConfigInputFragment hintDialogFragment;
    TextView tv_settings;
    EditText et_device_code, et_screen_num;

    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ConfigInputFragment newInstance() {
//        Bundle args = new Bundle();
//        args.putString("hint_msg", hint_msg);
//        args.putBoolean("display_cancel", displayCancel);
//        args.putBoolean("display_confirm", displayConfirm);
//        ConfigInputFragment fragment = new ConfigInputFragment();
//        fragment.setArguments(args);
        return new ConfigInputFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_config, container, false);
        tv_settings = view.findViewById(R.id.tv_settings);
        et_device_code = view.findViewById(R.id.et_device_code);
        et_screen_num = view.findViewById(R.id.et_screen_num);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
        view.findViewById(R.id.tv_settings).setOnClickListener(this);
        showAndHideSoft(true);
        view.requestFocus();
        view.setClickable(false);
        view.setOnTouchListener(this);
       /* if (new File(Constants.AD_CONFIG_CACHE_DIR).exists()) {
            String configInfo = FileUtil.readFileSdcardFile(Constants.AD_CONFIG_CACHE_DIR);
            if(!TextUtils.isEmpty(configInfo)){
                String[] configs = configInfo.split("&");
                et_device_code.setText(configs[0]);
                et_device_code.setSelection(configs[0].length());
                et_screen_num.setText(configs[1]);
            }
        }*/
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public static void showDialog(FragmentActivity main2Activity, Callback callback) {
        dismissDialog();
        FragmentManager fragmentManager = main2Activity.getSupportFragmentManager();
        hintDialogFragment = ConfigInputFragment.newInstance();
        hintDialogFragment.setCallback(callback);
        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, hintDialogFragment)
                .addToBackStack(null).commitAllowingStateLoss();
//        deviceTypeDialogFragment.show(fragmentManager, "dialog");
    }

    public static void dismissDialog() {
        if (hintDialogFragment != null) {
            hintDialogFragment.dismissAllowingStateLoss();
            hintDialogFragment = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (handleConfirmConfigInfo()) return;
        }
        if (v.getId() == R.id.tv_settings) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
        dismissDialog();
    }

    private boolean handleConfirmConfigInfo() {
        String deviceCode = et_device_code.getText().toString().trim();
        String screenNo = et_screen_num.getText().toString().trim();
        if (isEmpty(deviceCode)) {
            Toast.makeText(getActivity(), "请输入设备编号", Toast.LENGTH_SHORT);
            return true;
        }
        if (isEmpty(screenNo)) {
            Toast.makeText(getActivity(), "请输入屏幕编号", Toast.LENGTH_SHORT);
            return true;
        }
        //保存配置
        String configInfo = "";
//        if(new File(Constants.AD_CONFIG_CACHE_DIR).exists()){
//            configInfo = FileUtil.readFileSdcardFile(Constants.AD_CONFIG_CACHE_DIR);
//            String[] configs = configInfo.split("&");
//            if(configs.length > 2){
//                configInfo = configs[0] + "&";
//            }else{
//                configInfo = "";
//            }
//        }
        configInfo = deviceCode + "&" + screenNo;
        FileUtil.writeFileSdcardFile(Constants.AD_CONFIG_CACHE_DIR, configInfo);
        callback.onConfirm(configInfo);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showAndHideSoft(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public interface Callback {
        void onConfirm(String configInfo);
    }

    private void showAndHideSoft(boolean show) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(show){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }else{
            imm.hideSoftInputFromWindow(et_device_code.getWindowToken(), 0);
        }
    }
}
