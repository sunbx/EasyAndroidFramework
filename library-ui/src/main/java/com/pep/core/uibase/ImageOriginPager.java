package com.pep.core.uibase;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 自定义viewpager,解决viewpager和photoView造成的IllegalArgumentException
 * java.lang.IllegalArgumentException: pointerIndex out of range pointerIndex=-1 pointerCount=1
 */
public class ImageOriginPager extends ViewPager {
    public ImageOriginPager(Context context) {
        super (context);
    }
    public ImageOriginPager(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            boolean b = super.onInterceptTouchEvent(event);
            return b;
        } catch (IllegalArgumentException  e) {
            e.printStackTrace();
        }
        return false ;
    }


}

