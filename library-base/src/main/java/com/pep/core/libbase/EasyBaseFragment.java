package com.pep.core.libbase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pep.core.uibase.EasyProgressView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class EasyBaseFragment extends Fragment implements View.OnClickListener {

    public LinearLayout rootView;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = View.inflate(getActivity(), getLayoutId(), null);
        rootView = (LinearLayout) findViewById(R.id.rootView);
        initView();
        initData();
        initListener();

        return contentView;
    }

    public void addContentView(View contentView) {
        rootView.removeAllViews();
        rootView.addView(contentView);
    }


    public View findViewById(int id) {
        return contentView.findViewById(id);
    }


    public int getLayoutId() {
        return R.layout.activity_root;
    }

    public abstract void initView();

    public abstract void initData();

    public void initListener() {
    }

    public void showProgress() {
        if (getActivity() != null) {
            EasyProgressView.show(getActivity());
        }


    }

    public void dismissProgress() {
        EasyProgressView.dismiss();

    }

    @Override
    public void onClick(View view) {

    }
}
