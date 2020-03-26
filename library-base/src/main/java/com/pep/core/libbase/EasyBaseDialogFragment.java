package com.pep.core.libbase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nineoldandroids.animation.Animator;
import com.pep.core.uibase.EasyDialogAnimate;
import com.pep.core.uibase.TouchLinLayout;

import java.util.Objects;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * @author sunbaixin QQ:283122529
 * @name PEPCore_Android
 * @class name：com.pep.core.libbase
 * @class describe
 * @time 2019-10-30 11:17
 * @change
 * @chang time
 * @class describe
 */
public abstract class EasyBaseDialogFragment extends DialogFragment {
    public ViewGroup contentView;

    @Override
    public void onStart() {
        super.onStart();
        EasyDialogAnimate.initWindow(this, getAnimateStart());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.pep.core.uibase.R.style.CommonDialog);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
        switch (getAnimateStart()) {
            case Gravity.TOP:
                EasyDialogAnimate.topStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.BOTTOM:
                EasyDialogAnimate.bottomStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.LEFT:
                EasyDialogAnimate.leftStartAnimate(contentView, isAnimateKick());
                break;
            case Gravity.RIGHT:
                EasyDialogAnimate.rightStartAnimate(contentView, isAnimateKick());
                break;
            default:
        }
        final TouchLinLayout content = contentView.findViewWithTag("TouchLinLayout");

        if (content != null && isAnimateTouch()) {
            content.setAnimateKick(isAnimateKick());
            content.setDirection(getAnimateStart());
            content.setAnimatorListener(animatorListener);
        }


        Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);

        initView();
        initData();

        return contentView;
    }

    public View findViewById(int id) {
        return contentView.findViewById(id);
    }


    protected abstract int getAnimateStart();

    protected boolean isAnimateKick() {
        return false;
    }

    protected boolean isAnimateTouch() {
        return false;
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public void initListener() {

    }


    public void close() {
        //执行关闭动画
        dismiss();
    }

    public void close(int animateStart) {
        switch (animateStart) {
            case Gravity.TOP:
                EasyDialogAnimate.topCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.BOTTOM:
                EasyDialogAnimate.bottomCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.LEFT:
                EasyDialogAnimate.leftCloseAnimate(contentView, animatorListener);
                break;
            case Gravity.RIGHT:
                EasyDialogAnimate.rightCloseAnimate(contentView, animatorListener);
                break;
            default:
        }
    }


    /**
     * The Dialog interface.
     */
    private DialogInterface.OnKeyListener dialogInterface = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(final DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            //监听dialog 返回键
            if (KEYCODE_BACK == i && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //执行关闭动画
                close(getAnimateStart());


                return true;
            }
            return false;
        }
    };

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.e("TAG", "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            //动画执行完成进行关闭dialogfragment
            dismiss();
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            Log.e("TAG", "onAnimationCancel");
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            Log.e("TAG", "onAnimationRepeat");
        }
    };

}
