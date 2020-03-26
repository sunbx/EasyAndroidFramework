package com.pep.core.uibase;

import android.content.Context;


/**
 * The type Dialog loading.菊花操作类
 *
 * @author sunbaixin
 */
public class EasyProgressView {


    private static ProgressLoadingView dialog;

    /**
     * Show.显示菊花
     *
     * @param coxtext the coxtext
     */
    public static void show(Context coxtext) {
        if (coxtext == null) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        ProgressLoadingView.Builder builder = new ProgressLoadingView.Builder(coxtext);
        dialog = builder.create();
        dialog.show();
    }

    /**
     * Dismiss.隐藏菊花
     */
    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
            dialog = null;
    }
}
