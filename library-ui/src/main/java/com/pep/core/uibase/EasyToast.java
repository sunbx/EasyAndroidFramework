package com.pep.core.uibase;

import android.content.Context;
import android.widget.Toast;

/**
 * @author sunbaixin QQ:283122529
 * @name pepCore
 * @class name：com.pep.core.uibase
 * @class describe
 * @time 2019-09-26 14:59
 * @change
 * @chang time
 * @class describe
 */
public class EasyToast {
    public static void show(Context context, String text) {
        Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
