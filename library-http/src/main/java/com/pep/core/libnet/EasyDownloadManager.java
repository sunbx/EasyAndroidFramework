package com.pep.core.libnet;


import com.downloader.PRDownloader;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;

/**
 * The type Download manager.
 *
 * @author sunbaixin
 */
public class EasyDownloadManager {

    /**
     * Down load download request. 下载文件
     *
     * @param url      the url 要下载的url
     * @param dirPath  the dir path 要下载的本地存放路径
     * @param fileName the file name 保存后的名字
     * @return the download request 返回下载实体信息
     */
    public static DownloadRequest downLoad(String url, String dirPath, String fileName) {
        DownloadRequest downloadRequest = PRDownloader.download(url, dirPath, fileName)
                .build();
        return downloadRequest;
    }

    /**
     * Cancel.
     *
     * @param downloadRequest the download request
     */
    public static void cancel(DownloadRequest downloadRequest) {
        if (downloadRequest == null) {
            return;
        }
        PRDownloader.cancel(downloadRequest);

    }

    /**
     * Cancel all.
     */
    public static void cancelAll() {
        PRDownloader.cancelAll();
    }

    /**
     * Pause.
     *
     * @param downloadId the download request
     */
    public static void pause(int downloadId) {

        PRDownloader.pause(downloadId);
    }

    /**
     * Resume.
     *
     * @param downloadId the download request
     */
    public static void resume(int  downloadId) {
        PRDownloader.resume(downloadId);
    }

    /**
     * Gets status.
     *
     * @param downLoadId the download request
     * @return the status
     */
    public static Status getStatus(int downLoadId) {
        if (downLoadId == 0) {
            return Status.FAILED;
        }
        Status status = PRDownloader.getStatus(downLoadId);
        return status;
    }

}
