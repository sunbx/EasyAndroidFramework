package com.pep.core.libav;

import com.google.android.exoplayer2.ExoPlaybackException;


/**
 * The interface Pep audio listener.
 */
public interface EasyAudioListener {


    /**
     * On play state. 当前播放状态
     *
     * @param isPlay the is play
     */
    void onPlayState(boolean isPlay);

    /**
     * On buffering.缓冲中
     */
    void onBuffering();

    /**
     * On play end.播放结束
     */
    void onPlayEnd();

    /**
     * On play ready.准备播放
     */
    void onPlayReady();

    /**
     * On player error.播放出错
     *
     * @param error the error
     */
    void onPlayerError(ExoPlaybackException error);

    /**
     * On progress. 实时进度回调
     *
     * @param progress     the progress 当前进度
     * @param bprogress    the bprogress 缓冲的进度
     * @param maxProgress  the max progress 进度最大值 1000
     * @param currentTime  the current time 当前播放的时间戳 00:00格式
     * @param durationTime the duration time 当前资源最大可播放时间 00:00格式
     */
    void onProgress(int progress, int bprogress, int maxProgress, String currentTime, String durationTime);
}
