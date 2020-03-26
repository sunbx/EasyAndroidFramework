package com.pep.core.libbase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.pep.core.uibase.EasyProgressView;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * @author sunbaixin
 */
public abstract class EasyBaseActivity extends SwipeBackActivity implements View.OnClickListener {
    public Activity mContext;
    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();

        swipeBackLayout.setEnableGesture(isSwipeBack());


        setContentView(getLayoutId());
        rootView = (LinearLayout) findViewById(R.id.rootView);
        initStatus();

        initView();
        initData();
        initListener();
    }

    public void addContentView(View contentView) {
        rootView.removeAllViews();
        rootView.addView(contentView);
    }

    private void initStatus() {
        StatusBarUtil.setColor(this, 0x000000);
    }

    public boolean isSwipeBack() {
        return true;
    }

    public int getLayoutId(){
        return R.layout.activity_root;
    }

    public abstract void initView();

    public abstract void initData();

    public void initListener() {
    }

    public void showProgress() {
        EasyProgressView.show(this);

    }

    public void dismissProgress() {
        EasyProgressView.dismiss();

    }

    @Override
    public void onClick(View view) {

    }


    //===================点击空白处,关闭键盘=======================
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                boolean res = hideKeyboard(v.getWindowToken());
                if (res) {
                    //隐藏了输入法，则不再分发事件
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    private boolean hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }
}
