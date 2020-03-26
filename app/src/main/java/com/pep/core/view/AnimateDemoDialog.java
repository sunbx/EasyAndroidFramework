package com.pep.core.view;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pep.core.R;
import com.pep.core.libbase.EasyBaseDialogFragment;


public class AnimateDemoDialog extends EasyBaseDialogFragment {

    private View contentView;


    @Override
    protected int getAnimateStart() {
        return Gravity.LEFT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_left_demo;
    }

    @Override
    protected boolean isAnimateKick() {
        return true;
    }

    @Override
    protected boolean isAnimateTouch() {
        return true;
    }

    @Override
    public void initView() {
        ListView lvView = (ListView) findViewById(R.id.lv_view);
        lvView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(getActivity());
                textView.setText("position:" + i);
                return textView;
            }
        });


    }

    @Override
    public void initData() {

    }


}
