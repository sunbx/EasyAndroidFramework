package com.pep.core.libcommon;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * @author sunbaixin QQ:283122529
 * @name pepCore
 * @class name：com.pep.core.libcommon
 * @class describe
 * @time 2019-09-18 13:57
 * @change
 * @chang time
 * @class describe
 */
public class EasyDisplayUtil {
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