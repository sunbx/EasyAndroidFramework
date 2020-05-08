package com.pep.core.uibase;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

public class EasyLoadingView extends RelativeLayout {

    public static final int EASY_LOADING_LOAD   = 1;
    public static final int EASY_LOADING_ERROR  = 2;
    public static final int EASY_LOADING_FINISH = 3;

    private int currentLoading = -1;

    private Context mContext;
    private View layoutLoadingLoad;
    private View layoutLoadingError;


    private OnLoadingErrorListener onErrorListener;
    private ImageView iv_progress;

    public EasyLoadingView(Context context) {
        super(context);
        this.mContext = context;
    }

    public EasyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public EasyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setOnErrorListener(OnLoadingErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }


    public interface OnLoadingErrorListener {
        void onClick();
    }


    public void setLoadingType(int loadingType) {
        if (layoutLoadingLoad == null || layoutLoadingError == null) {
            layoutLoadingLoad = View.inflate(mContext, R.layout.layout_loading_load, null);
            layoutLoadingError = View.inflate(mContext, R.layout.layout_loading_error, null);

            layoutLoadingLoad.setOnClickListener(null);
            layoutLoadingError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLoadingType(EASY_LOADING_LOAD);
                    if (onErrorListener != null) {
                        onErrorListener.onClick();
                    }
                }
            });
            addView(layoutLoadingError);
            addView(layoutLoadingLoad);
            LayoutParams layout_loading_loading_params = (LayoutParams) layoutLoadingLoad.getLayoutParams();
            layout_loading_loading_params.height = getScreenHeight(mContext);
            layout_loading_loading_params.width = getScreenWidth(mContext);

            LayoutParams layout_loading_error_params = (LayoutParams) layoutLoadingError.getLayoutParams();
            layout_loading_error_params.height = getScreenHeight(mContext);
            layout_loading_error_params.width = getScreenWidth(mContext);

        }


        switch (loadingType) {
            case EASY_LOADING_LOAD:
                ObjectAnimator.ofFloat(layoutLoadingLoad, "alpha", 0f, 1f).setDuration(0).start();
                layoutLoadingLoad.setVisibility(View.VISIBLE);
                layoutLoadingError.setVisibility(View.GONE);
                break;
            case EASY_LOADING_ERROR:
                layoutLoadingLoad.setVisibility(View.GONE);
                layoutLoadingError.setVisibility(View.VISIBLE);
                break;
            case EASY_LOADING_FINISH:
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        layoutLoadingLoad.setVisibility(View.GONE);
                        layoutLoadingError.setVisibility(View.GONE);
//                        removeView(layoutLoadingError);
//                        removeView(layoutLoadingLoad);
//                        layoutLoadingLoad = null;
//                        layoutLoadingError = null;
                        return false;
                    }

                }).sendEmptyMessageDelayed(0, 1000);


                ObjectAnimator.ofFloat(layoutLoadingLoad, "alpha", 1f, 0f).setDuration(1000).start();
                break;
            default:
        }
    }


    /**
     * 获取屏幕宽高
     */
    public int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
