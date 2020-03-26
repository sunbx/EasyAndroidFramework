package com.pep.core.uibase;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author sunbaixin QQ:283122529
 * @name AndroidBaseFrame
 * @class name：com.pep.core.uibase
 * @class describe
 * @time 2019-11-14 09:11
 * @change
 * @chang time
 * @class describe
 */
public class TouchLinLayout extends LinearLayout {
    public TouchLinLayout(Context context) {
        super(context);
        setTag("TouchLinLayout");
    }

    public TouchLinLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTag("TouchLinLayout");
    }

    public TouchLinLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTag("TouchLinLayout");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (direction == Gravity.TOP || direction == Gravity.BOTTOM) {
             return super.dispatchTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                isLeftRight = 0;
                Log.e("TouchLinLayout", " dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLeftRight == 1) {
                    onTouchThisEvent(motionEvent);
                    Log.e("TouchLinLayout", " onInterceptTouchEvent  33333333333333333333");
                    return true;
                } else if (isLeftRight == 2) {
                    Log.e("TouchLinLayout", " onInterceptTouchEvent  444444444444444444444444");
                    return super.dispatchTouchEvent(motionEvent);

                } else {
                    tslideY = downY - (int) motionEvent.getRawY();
                    tslideX = downX - (int) motionEvent.getRawX();
                    int absX = Math.abs(tslideX);
                    int absY = Math.abs(tslideY);
                    if (absX > absY) {
                        Log.e("TouchLinLayout", " dispatchTouchEvent ACTION_MOVE 1111111111111111");
                        isLeftRight = 1;
                    } else {
                        isLeftRight = 2;
                        Log.e("TouchLinLayout", " dispatchTouchEvent ACTION_MOVE 22222222222222222");
                    }
                }


            case MotionEvent.ACTION_UP:
                Log.e("TouchLinLayout", " dispatchTouchEvent ACTION_UP 111111111111111111111");
                if (isLeftRight == 1) {
                    onTouchThisEvent(motionEvent);
                }

                break;
            default:

        }

//        return true;
        return super.dispatchTouchEvent(motionEvent);
    }

//    //
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
//        if (isLeftRight == 1) {
////                    onTouchEvent(motionEvent);
//            Log.e("TouchLinLayout", " onInterceptTouchEvent  1111111111111111111111");
//            return true;
//        } else if (isLeftRight == 2) {
////                    return super.onInterceptTouchEvent(motionEvent);
//
//        }
//        Log.e("TouchLinLayout", " onInterceptTouchEvent  22222222222222222");
//        return false;
//    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                tdownX = (int) motionEvent.getRawX();
//                tdownY = (int) motionEvent.getRawY();
//                isLeftRight = 0;
//                Log.e("TouchLinLayout", "ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("TouchLinLayout", "ACTION_MOVE");
//                if (isLeftRight == 1) {
////                    onTouchEvent(motionEvent);
//                    return true;
//                } else if (isLeftRight == 2) {
//                    return super.onInterceptTouchEvent(motionEvent);
//                } else {
//                    tslideY = tdownY - (int) motionEvent.getRawY();
//                    tslideX = tdownX - (int) motionEvent.getRawX();
//                    int absX = Math.abs(tslideX);
//                    int absY = Math.abs(tslideY);
//                    if (absX > absY) {
//                        isLeftRight = 1;
//                    } else {
//                        isLeftRight = 2;
//                    }
//                    Log.e("TouchLinLayout", "tslideX:" + absX + "----tslideY:" + absY);
//                }
//
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//            default:
//
//        }
//
//        return true;
//    }
//

    private int     direction;
    private boolean isAnimateKick;

    public boolean onTouchThisEvent(MotionEvent motionEvent) {
        if (isLeftRight == 0 ) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                slideY = downY - (int) motionEvent.getRawY();
                slideX = downX - (int) motionEvent.getRawX();

                if (direction == Gravity.TOP) {
                    if (slideY >= 0) {
                        if (isAnimateKick) {
                            ViewHelper.setY(this, -slideY / 4);
                            Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        }
                    } else {
                        ViewHelper.setY(this, -slideY);
                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        if (isAnimateKick) {
                            ViewHelper.setRotation(this, -slideY / 100);
                            Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        }
                    }
                }


                if (direction == Gravity.BOTTOM) {
                    if (slideY >= 0) {
                        if (isAnimateKick) {
                            ViewHelper.setY(this, -slideY / 4);
                            Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        }
                    } else {
                        ViewHelper.setY(this, -slideY);
                        Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        if (isAnimateKick) {
                            ViewHelper.setRotation(this, -slideY / 100);
                            Log.e("PEPBaseDialogFragment", "slideX:" + slideY);
                        }
                    }
                }

                if (direction == Gravity.LEFT) {
                    if (slideX >= 0) {
                        ViewHelper.setX(this, -slideX);
                        Log.e("PEPBaseDialogFragment", "slideX:" + -slideX);
                    }
                }

                if (direction == Gravity.RIGHT) {
                    if (slideX <= 0) {
                        ViewHelper.setX(this, -slideX);
                        Log.e("PEPBaseDialogFragment", "slideX:" + -slideX);
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                if (direction == Gravity.LEFT) {
                    if (slideX > 200) {
                        ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", -slideX, -EasyDialogAnimate.getScreenWidth(this.getContext())).setDuration(300);
                        translationX.addListener(animatorListener);
                        translationX.start();
                    } else {
                        if (slideX > 0) {
                            ObjectAnimator.ofFloat(this, "translationX", -slideX, 0).setDuration(300).start();
                        }

                    }
                }

                if (direction == Gravity.RIGHT) {
                    if (slideX < -200) {
                        ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", -slideX, EasyDialogAnimate.getScreenWidth(this.getContext())).setDuration(300);
                        translationX.addListener(animatorListener);
                        translationX.start();
                    } else {
                        if (slideX < 0) {
                            ObjectAnimator.ofFloat(this, "translationX", -slideX, 0).setDuration(300).start();
                        }

                    }
                }


                if (direction == Gravity.TOP) {
                    if (slideY >= 0) {
                        if (isAnimateKick) {
                            ObjectAnimator.ofFloat(this, "translationY", -slideY / 4, 0).setDuration(300).start();
                        }
                    } else {
                        if (slideY > -200) {
                            AnimatorSet set = new AnimatorSet();
                            if (isAnimateKick) {
                                set.playTogether(
                                        ObjectAnimator.ofFloat(this, "rotation", -slideY / 100, 0),
                                        ObjectAnimator.ofFloat(this, "translationY", -slideY, 0)
                                );
                            } else {
                                set.playTogether(
                                        ObjectAnimator.ofFloat(this, "translationY", -slideY, 0)
                                );
                            }

                            set.setDuration(300).start();
                        } else {
                            ViewHelper.setY(this, -slideY);
                            ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationY", -slideY, EasyDialogAnimate.getScreenHeight(this.getContext())).setDuration(300);
                            translationY.addListener(animatorListener);
                            translationY.start();
                        }

                    }
                }


                if (direction == Gravity.BOTTOM) {
                    if (slideY >= 0) {
                        if (isAnimateKick) {
                            ObjectAnimator.ofFloat(this, "translationY", -slideY / 4, 0).setDuration(300).start();
                        }
                    } else {
                        if (slideY > -200) {
                            AnimatorSet set = new AnimatorSet();
                            if (isAnimateKick) {
                                set.playTogether(
                                        ObjectAnimator.ofFloat(this, "rotation", -slideY / 100, 0),
                                        ObjectAnimator.ofFloat(this, "translationY", -slideY, 0)
                                );
                            } else {
                                set.playTogether(
                                        ObjectAnimator.ofFloat(this, "translationY", -slideY, 0)
                                );
                            }

                            set.setDuration(300).start();
                        } else {
                            ViewHelper.setY(this, -slideY);
                            ObjectAnimator translationY = ObjectAnimator.ofFloat(this, "translationY", -slideY, EasyDialogAnimate.getScreenHeight(this.getContext())).setDuration(300);
                            translationY.addListener(animatorListener);
                            translationY.start();
                        }

                    }
                }
                break;
            default:
        }
        return true;
    }


    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setAnimateKick(boolean animateKick) {
        isAnimateKick = animateKick;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public Animator.AnimatorListener animatorListener;

    private int downY  = 0;//按下时的点
    private int downX  = 0;//按下时的点
    private int slideY = 0;//最终移动距离
    private int slideX = 0;//最终移动距离


    private int isLeftRight;
    //    private int tdownY  = 0;
//    private int tdownX  = 0;
    private int tslideY = 0;
    private int tslideX = 0;

}