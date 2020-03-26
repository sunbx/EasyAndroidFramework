package com.pep.core.uibase;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class EasyDialogAnimate {


    public static void leftStartAnimate(View view, boolean isKick) {
        if (isKick) {
            ObjectAnimator.ofFloat(view, "translationX", -getScreenWidth(view.getContext()), 150, -80, 0).setDuration(1000).start();
        } else {
            ObjectAnimator.ofFloat(view, "translationX", -getScreenWidth(view.getContext()), 0).setDuration(500).start();
        }
    }

    public static void rightStartAnimate(View view, boolean isKick) {
        if (isKick) {
            ObjectAnimator.ofFloat(view, "translationX", getScreenWidth(view.getContext()), -150, 80, 0).setDuration(1000).start();
        } else {
            ObjectAnimator.ofFloat(view, "translationX", getScreenWidth(view.getContext()), 0).setDuration(500).start();
        }
    }

    public static void topStartAnimate(View view, boolean isKick) {
        if (isKick) {
            ObjectAnimator.ofFloat(view, "translationY", -getScreenHeight(view.getContext()), 150, -80, 0).setDuration(1000).start();
            ObjectAnimator.ofFloat(view, "rotation", -30, 0).setDuration(400).start();

        } else {
            ObjectAnimator.ofFloat(view, "translationY", -getScreenHeight(view.getContext()), 0).setDuration(500).start();

        }
    }

    public static void bottomStartAnimate(View view, boolean isKick) {
        if (isKick) {
            ObjectAnimator.ofFloat(view, "translationY", getScreenHeight(view.getContext()), -150, 80, 0).setDuration(1000).start();
            ObjectAnimator.ofFloat(view, "rotation", 30, 0).setDuration(400).start();
        } else {
            ObjectAnimator.ofFloat(view, "translationY", getScreenHeight(view.getContext()), 0).setDuration(500).start();
        }
    }


    public static void leftCloseAnimate(View view, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, -getScreenWidth(view.getContext())).setDuration(500);
        objectAnimator.addListener(listener);
        objectAnimator.start();
    }

    public static void rightCloseAnimate(View view, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, getScreenWidth(view.getContext())).setDuration(500);
        objectAnimator.addListener(listener);
        objectAnimator.start();
    }

    public static void topCloseAnimate(View view, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, -getScreenHeight(view.getContext())).setDuration(500);
        objectAnimator.addListener(listener);
        objectAnimator.start();

    }

    public static void bottomCloseAnimate(View view, com.nineoldandroids.animation.Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, getScreenHeight(view.getContext())).setDuration(500);
        objectAnimator.addListener(listener);
        objectAnimator.start();
    }

    public static void initWindow(DialogFragment dialogFragment, int gravity) {
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = dialogFragment.getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        dialogFragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = gravity;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    /**
     * Get Screen Width
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * Get Screen Height
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
