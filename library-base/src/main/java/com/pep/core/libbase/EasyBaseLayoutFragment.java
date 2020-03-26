package com.pep.core.libbase;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public abstract class EasyBaseLayoutFragment<F extends EasyBaseFragment, P extends EasyBasePresenter> extends RelativeLayout implements View.OnClickListener {
    public F fragment;
    public P presenter;

    public EasyBaseLayoutFragment(Context context) {
        super(context);
    }

    public EasyBaseLayoutFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyBaseLayoutFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        View.inflate(getContext(), getLayoutId(), this);
        initView();
        initData();
        initListener();
    }


    public void setPresenter(F fragment, P presenter) {
        this.fragment = fragment;
        this.presenter = presenter;
        init();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();


    public abstract int getLayoutId();


    @Override
    public void onClick(View view) {

    }
}
