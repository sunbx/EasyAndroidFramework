package com.pep.core.uibase;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


/**
 * The type Progress loading.通用loading 菊花
 *
 * @author sunbaixin
 */
public class ProgressLoadingView extends Dialog {

    /**
     * Instantiates a new Progress loading.
     *
     * @param context the context
     */
    public ProgressLoadingView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Progress loading.
     *
     * @param context the context
     * @param theme   the theme
     */
    public ProgressLoadingView(Context context, int theme) {
        super(context, theme);
    }


    /**
     * The type Builder.
     */
    public static class Builder {

        private Context context;

        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        /**
         * Instantiates a new Builder.
         *
         * @param context the context
         */
        public Builder(Context context) {
            this.context = context;
        }


        /**
         * Sets cancelable.设置是否支持返回键关闭
         *
         * @param isCancelable the is cancelable
         * @return the cancelable
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }


        /**
         * Sets cancel outside.设置是否支持触摸关闭
         *
         * @param isCancelOutside the is cancel outside
         * @return the cancel outside
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        /**
         * Create progress loading.创建具体显示的loading
         *
         * @return the progress loading
         */
        public ProgressLoadingView create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_progress_loading, null);
            ProgressLoadingView loadingDailog = new ProgressLoadingView(context, R.style.ProgressLoading);
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;
        }
    }
}
