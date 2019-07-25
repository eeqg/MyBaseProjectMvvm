package com.example.wp.resource.basic;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.wp.resource.R;
import com.example.wp.resource.common.LoadingDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by wp on 2019/5/8.
 */
public abstract class BasicDialogFragment<B extends ViewDataBinding> extends DialogFragment
        implements BasicViewImp {

    protected B dataBinding;
    private boolean fullScreen;
    private boolean bottomAnimation;
    private boolean transparent;
    private boolean canceledTouchOutside = true;
    private LoadingDialog loadingDialog;

    public void setFullScreen() {
        this.fullScreen = true;
    }

    public void setCanceledTouchOutside(boolean canceledTouchOutside) {
        this.canceledTouchOutside = canceledTouchOutside;
    }

    protected void setBottomAnimation() {
        this.bottomAnimation = true;
    }

    protected void setTransparent() {
        this.transparent = true;
    }

    protected void showLoading() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void hideLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void promptMessage(int resId) {
        promptMessage(getString(resId));
    }

    protected void promptMessage(String msg) {
        BasicApp.toast(msg);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(getContext());
        init();
        initView();

        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            //transparent
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //width & height
            int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            int dialogHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
            window.setLayout(fullScreen ? WindowManager.LayoutParams.MATCH_PARENT : dialogWidth,
                    fullScreen ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT);

            getDialog().setCanceledOnTouchOutside(canceledTouchOutside); //点击边际可消失

            // getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            //animation
            if (bottomAnimation) {
                window.setWindowAnimations(R.style.AnimationBottom);
                // window.getAttributes().windowAnimations = R.style.AnimationBottom;
            }
            if (transparent) {
                window.setDimAmount(0.0f);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
    }


    @Override
    public void dismiss() {
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
        super.dismiss();
    }


    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
