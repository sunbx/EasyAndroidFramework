package com.pep.core.libcommon;

import android.text.TextUtils;
import android.util.Log;


public class EasyLog {

    public static String baseLogTag = "PEP_LOG";

    /**
     * @desc 允许输出log的级别(0代表不输出, 5代表输出所有的日志 ; 1就是输出v, 2就是输出v和d, 3就是输出v, d, i ...)
     */
    private static int mDebuggable = 5;

    /**
     * 日志输出级别V
     */
    public static final int LEVEL_VERBOSE = 1;

    /**
     * 日志输出级别D
     */
    public static final int LEVEL_DEBUG = 2;

    /**
     * 日志输出级别I
     */
    public static final int LEVEL_INFO = 3;

    /**
     * 日志输出级别W
     */

    public static final int LEVEL_WARN = 4;

    /**
     * 日志输出级别E
     */
    public static final int LEVEL_ERROR = 5;

    /**
     * 以级别为 v 的形式输出LOG
     */
    public static void v(Object objTag, String msg) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            String tag;
            // 如果objTag是String，则直接使用;如果objTag不是String，则使用它的类名
            if (objTag instanceof String) {
                tag = (String) objTag;
            } else if (objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                //处理在匿名内部类中写this显示不出类名的问题
                tag = objTag.getClass().getName();
                String[] split = tag.split("\\.");
                tag = split[split.length - 1].split("\\$")[0];
            }
            //处理当objTag为null的情况
            if (TextUtils.isEmpty(tag)) {
                tag = "该Tag为空";
            }
            if (TextUtils.isEmpty(msg)) {
                Log.v(baseLogTag + " # " + tag, "该log输出信息为空");
            } else {
                Log.v(baseLogTag + " # " + tag, msg);
            }
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */

    public static void d(Object objTag, String msg) {
        if (mDebuggable >= LEVEL_DEBUG) {
            String tag;
            // 如果objTag是String，则直接使用;如果objTag不是String，则使用它的类名
            if (objTag instanceof String) {
                tag = (String) objTag;
            } else if (objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                //处理在匿名内部类中写this显示不出类名的问题
                tag = objTag.getClass().getName();
                String[] split = tag.split("\\.");
                tag = split[split.length - 1].split("\\$")[0];
            }

            if (TextUtils.isEmpty(tag)) {
                tag = "该Tag为空";
            }

            if (TextUtils.isEmpty(msg)) {
                Log.d(baseLogTag + " # " + tag, "该log输出信息为空");
            } else {
                Log.d(baseLogTag + " # " + tag, msg);
            }
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    public static void i(Object objTag, String msg) {
        if (mDebuggable >= LEVEL_INFO) {
            String tag;
            // 如果objTag是String，则直接使用
            // 如果objTag不是String，则使用它的类名
            if (objTag instanceof String) {
                tag = (String) objTag;
            } else if (objTag instanceof Class) {
                tag = ((Class) objTag).getSimpleName();
            } else {
                tag = objTag.getClass().getName();
                String[] split = tag.split("\\.");
                tag = split[split.length - 1].split("\\$")[0];
            }

            if (TextUtils.isEmpty(tag)) {
                tag = "该Tag为空";
            }
            if (TextUtils.isEmpty(msg)) {
                Log.i(baseLogTag + " # " + tag, "该log输出信息为空");
            } else {
                Log.i(baseLogTag + " # " + tag, msg);

            }

        }

    }

    /**
     * 以级别为 w 的形式输出LOG
     */

    public static void w(Object objTag, String msg) {
        if (mDebuggable >= LEVEL_WARN) {
            String tag;
// 如果objTag是String，则直接使用
// 如果objTag不是String，则使用它的类名

            if (objTag instanceof String) {

                tag = (String) objTag;

            } else if (objTag instanceof Class) {

                tag = ((Class) objTag).getSimpleName();

            } else {

                tag = objTag.getClass().getName();

                String[] split = tag.split("\\.");

                tag = split[split.length - 1].split("\\$")[0];

            }

            if (TextUtils.isEmpty(tag)) {

                tag = "该Tag为空";

            }

            if (TextUtils.isEmpty(msg)) {

                Log.w(baseLogTag + " # " + tag, "该log输出信息为空");

            } else {

                Log.w(baseLogTag + " # " + tag, msg);

            }

        }

    }

    /**
     * 以级别为 e 的形式输出LOG
     */

    public static void e(Object objTag, String msg) {

        if (mDebuggable >= LEVEL_ERROR) {

            String tag;

            // 如果objTag是String，则直接使用

            // 如果objTag不是String，则使用它的类名

            if (objTag instanceof String) {

                tag = (String) objTag;

            } else if (objTag instanceof Class) {

                tag = ((Class) objTag).getSimpleName();

            } else {

                tag = objTag.getClass().getName();

                String[] split = tag.split("\\.");

                tag = split[split.length - 1].split("\\$")[0];

            }

            if (TextUtils.isEmpty(tag)) {

                tag = "该Tag为空";

            }

            if (TextUtils.isEmpty(msg)) {

                Log.e(baseLogTag + " # " + tag, "该log输出信息为空");

            } else {

                Log.e(baseLogTag + " # " + tag, msg);

            }

        }

    }
}

