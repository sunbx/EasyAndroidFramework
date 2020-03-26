package com.pep.core.libcommon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author sunbaixin
 */
public class EasyThreadTool {
    /**
     * 下载书籍串行并列执行线程池
     */

    public static ExecutorService executorServiceSingle = Executors.newSingleThreadExecutor();

    /**
     * 公用线程池
     */
    public static ExecutorService executorServiceFixed = Executors.newFixedThreadPool(32);


}
