package com.pep.core.libcommon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * 隐私权限判断和申请 工具类
 */
public final class EasyPermissionUtils {

    private static EasyPermissionUtils sInstance;
    private static Context             mContext;

    private SimpleCallback      mSimpleCallback;
    private FullCallback        mFullCallback;
    private Set<String> mPermissions;
    private List<String> mPermissionsRequest;
    private List<String> mPermissionsGranted;
    private List<String> mPermissionsDenied;
    private List<String> mPermissionsDeniedForever;

    private static SimpleCallback sSimpleCallback4WriteSettings;
    private static SimpleCallback sSimpleCallback4DrawOverlays;

    /**
     * 获取使用的权限列表
     */
    public static List<String> getPermissions(Context context) {
        return getPermissions(context.getPackageName());
    }

    /**
     * 获取使用的权限列表
     */
    private static List<String> getPermissions(final String packageName) {
        PackageManager pm = mContext.getPackageManager();
        try {
            String[] permissions = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions;
            if (permissions == null) return Collections.emptyList();
            return Arrays.asList(permissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     *  是否已经授权
     */
    public static boolean isGranted(Context context,final String... permissions) {
        mContext = context;
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(mContext, permission);
    }

    /**
     * 是否可以修改系统设置
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedWriteSettings() {
        return Settings.System.canWrite(mContext);
    }

    /**
     * 打开系统页面-修改系统设置
     * @param context
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestWriteSettings(Context context,final SimpleCallback callback) {
        mContext =context;
        if (isGrantedWriteSettings()) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4WriteSettings = callback;
        PermissionActivity.start(mContext, PermissionActivity.TYPE_WRITE_SETTINGS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void startWriteSettingsActivity(final Activity activity, final int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        if (!isIntentAvailable(intent)) {
            launchAppDetailsSettings(mContext);
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 判断能否在其他应用上层显示
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isGrantedDrawOverlays(Context context) {
        mContext = context;
        return Settings.canDrawOverlays(mContext);
    }

    /**
     * 打开系统页面-在其他应用上层显示
     * @param context
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestDrawOverlays(Context context,final SimpleCallback callback) {
        mContext = context;
        if (isGrantedDrawOverlays(mContext)) {
            if (callback != null) callback.onGranted();
            return;
        }
        sSimpleCallback4DrawOverlays = callback;
        PermissionActivity.start(mContext, PermissionActivity.TYPE_DRAW_OVERLAYS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void startOverlayPermissionActivity(final Activity activity, final int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        if (!isIntentAvailable(intent)) {
            launchAppDetailsSettings(mContext);
            return;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开系统设置页面
     */
    public static void launchAppDetailsSettings(Context context) {
        mContext = context;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        if (!isIntentAvailable(intent)) return;
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 权限申请
     */
    public static EasyPermissionUtils permission(final Context context , final String... permissions) {
        EasyPermissionUtils.mContext = context.getApplicationContext();
        return new EasyPermissionUtils(permissions);
    }

    private static boolean isIntentAvailable(final Intent intent) {
        return mContext
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    private EasyPermissionUtils(final String... permissions) {
        mPermissions = new LinkedHashSet<>();
        List<String> permissionsList = getPermissions(mContext);
        for (String permission : permissions) {
            if (permissionsList.contains(permission)) {
                mPermissions.add(permission);
            }
        }
        sInstance = this;
    }
    /**
     *  设置回调
     */
    public EasyPermissionUtils callback(final SimpleCallback callback) {
        mSimpleCallback = callback;
        return this;
    }
    /**
     *  设置回调
     */
    public EasyPermissionUtils callback(final FullCallback callback) {
        mFullCallback = callback;
        return this;
    }

    /**
     * 开始请求
     */
    public void request() {
        mPermissionsGranted = new ArrayList<>();
        mPermissionsRequest = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionsGranted.addAll(mPermissions);
            requestCallback();
        } else {
            for (String permission : mPermissions) {
                if (isGranted(permission)) {
                    mPermissionsGranted.add(permission);
                } else {
                    mPermissionsRequest.add(permission);
                }
            }
            if (mPermissionsRequest.isEmpty()) {
                requestCallback();
            } else {
                startPermissionActivity();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startPermissionActivity() {
        mPermissionsDenied = new ArrayList<>();
        mPermissionsDeniedForever = new ArrayList<>();
        PermissionActivity.start(mContext, PermissionActivity.TYPE_RUNTIME);
    }

    private void getPermissionsStatus(final Activity activity) {
        for (String permission : mPermissionsRequest) {
            if (isGranted(permission)) {
                mPermissionsGranted.add(permission);
            } else {
                mPermissionsDenied.add(permission);
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    mPermissionsDeniedForever.add(permission);
                }
            }
        }
    }

    private void requestCallback() {
        if (mSimpleCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mSimpleCallback.onGranted();
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mSimpleCallback.onDenied();
                }
            }
            mSimpleCallback = null;
        }
        if (mFullCallback != null) {
            if (mPermissionsRequest.size() == 0
                    || mPermissions.size() == mPermissionsGranted.size()) {
                mFullCallback.onGranted(mPermissionsGranted);
            } else {
                if (!mPermissionsDenied.isEmpty()) {
                    mFullCallback.onDenied(mPermissionsDeniedForever, mPermissionsDenied);
                }
            }
            mFullCallback = null;
        }
    }

    private void onRequestPermissionsResult(final Activity activity) {
        getPermissionsStatus(activity);
        requestCallback();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static class PermissionActivity extends Activity {

        private static final String TYPE                = "TYPE";
        public static final  int    TYPE_RUNTIME        = 0x01;
        public static final  int    TYPE_WRITE_SETTINGS = 0x02;
        public static final  int    TYPE_DRAW_OVERLAYS  = 0x03;

        public static void start(final Context context, int type) {
            Intent starter = new Intent(context, PermissionActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            starter.putExtra(TYPE, type);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            int byteExtra = getIntent().getIntExtra(TYPE, TYPE_RUNTIME);
            if (byteExtra == TYPE_RUNTIME) {
                if (sInstance == null) {
                    super.onCreate(savedInstanceState);
                    finish();
                    return;
                }
                super.onCreate(savedInstanceState);

                if (sInstance.mPermissionsRequest != null) {
                    int size = sInstance.mPermissionsRequest.size();
                    if (size <= 0) {
                        finish();
                        return;
                    }
                    requestPermissions(sInstance.mPermissionsRequest.toArray(new String[size]), 1);
                }
            } else if (byteExtra == TYPE_WRITE_SETTINGS) {
                super.onCreate(savedInstanceState);
                startWriteSettingsActivity(this, TYPE_WRITE_SETTINGS);
            } else if (byteExtra == TYPE_DRAW_OVERLAYS) {
                super.onCreate(savedInstanceState);
                startOverlayPermissionActivity(this, TYPE_DRAW_OVERLAYS);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
            if (sInstance != null && sInstance.mPermissionsRequest != null) {
                sInstance.onRequestPermissionsResult(this);
            }
            finish();
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            finish();
            return true;
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == TYPE_WRITE_SETTINGS) {
                if (sSimpleCallback4WriteSettings == null) return;
                if (isGrantedWriteSettings()) {
                    sSimpleCallback4WriteSettings.onGranted();
                } else {
                    sSimpleCallback4WriteSettings.onDenied();
                }
                sSimpleCallback4WriteSettings = null;
            } else if (requestCode == TYPE_DRAW_OVERLAYS) {
                if (sSimpleCallback4DrawOverlays == null) return;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isGrantedDrawOverlays(mContext)) {
                            sSimpleCallback4DrawOverlays.onGranted();
                        } else {
                            sSimpleCallback4DrawOverlays.onDenied();
                        }
                        sSimpleCallback4DrawOverlays = null;
                    }
                },100);
            }
            finish();
        }
    }

    public interface SimpleCallback {
        void onGranted();

        void onDenied();
    }

    public interface FullCallback {
        void onGranted(List<String> permissionsGranted);

        void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied);
    }
}