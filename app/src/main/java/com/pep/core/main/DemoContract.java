package com.pep.core.main;

import com.downloader.Progress;
import com.pep.core.libav.EasyAudioManager;
import com.pep.core.libbase.EasyBaseView;
import com.pep.core.model.BaseListModel;
import com.pep.core.model.JokeModel;


/**
 * The type Demo contract.
 *
 * @author sunbaixin
 */
public class DemoContract {
    /**
     * The interface View.
     */
    interface View extends EasyBaseView<Presenter> {
        /**
         * Image load.图片加载
         */
        void imageLoad();

        /**
         * Progress loading.菊花加载
         */
        void progressLoading();

        /**
         * Layout loading.布局加载
         */
        void layoutLoading();

        /**
         * Progress update.更新进度条下载
         *
         * @param currentProgress the current progress 当前百分比
         * @param progress        the progress download 具体对象
         */
        void progressUpdate(int currentProgress, Progress progress);

        /**
         * Show progress. 显示菊花
         */
        void showProgress();

        /**
         * Dismiss progress. 隐藏菊花
         */
        void dismissProgress();

        /**
         * Update data. 更新接口数据
         *
         * @param baseListModel the base list model
         */
        void updateData(BaseListModel<JokeModel> baseListModel);

    }

    /**
     * The interface Presenter.
     */
    interface Presenter  {
        /**
         * Down start.开始下载
         */
        void downStart();

        /**
         * Down resume.恢复下载
         */
        void downResume();

        /**
         * Down pause.暂停下载
         */
        void downPause();

        /**
         * Net http.请求数据
         */
        void netHttp();

        void audioInitUrl(EasyAudioManager easyAudioManager, String url);
    }
}
