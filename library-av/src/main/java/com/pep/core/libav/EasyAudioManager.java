package com.pep.core.libav;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_DEF;
import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_M3U8;
import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_RTMP;

/**
 * The type Pep audio manager.
 */
public class EasyAudioManager {

    /**
     * Player url
     */
    private String url;

    /**
     * Player url type
     */
    private int playType;
    /**
     * exoplayer
     */
    private SimpleExoPlayer videoPlayer;
    /**
     * context
     */
    private Context context;

    /**
     * exoplayer view.
     */
    private PlayerView videoSurfaceView;

    /**
     * thread switch / yes / no
     */
    private boolean isThread = true;

    /**
     * Add pep audio listener.
     *
     * @param easyAudioListener the pep audio listener
     */
    public void addAudioListener(EasyAudioListener easyAudioListener) {
        this.easyAudioListener = easyAudioListener;
    }

    private EasyAudioListener easyAudioListener;

    /**
     * Instantiates a new Pep audio manager.
     *
     * @param context the context
     */
    public EasyAudioManager(Context context) {
        this.context = context;
    }

    /**
     * Sets url.
     *
     * @param url      the url
     * @param playType the play type
     */
    public void setUrl(String url, int playType) {
        this.url = url;
        this.playType = playType;
        initExoplayers(context);
    }


    /**
     * Pause.暂停
     */
    public void pause() {
        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(false);
            easyAudioListener.onPlayState(false);
        }
    }


    /**
     * Play.播放
     */
    public void play() {
        if (videoPlayer != null) {

            videoPlayer.setPlayWhenReady(true);
            easyAudioListener.onPlayState(true);
        }


    }

    /**
     * Release.是否资源
     */
    public void release() {
        videoPlayer.release();
        videoPlayer = null;
        isThread = false;
    }

    /**
     * Speed.跳转速度
     *
     * @param speed the speed
     */
    public void speed(float speed) {

        if (videoPlayer != null) {
            PlaybackParameters playbackParameters = new PlaybackParameters(speed);
            videoPlayer.setPlaybackParameters(playbackParameters);
        }

    }


    /**
     * Seek to.设置播放进度
     *
     * @param position the position
     */
    public void seekTo(int position) {
        if (videoPlayer != null) {
            videoPlayer.seekTo(position);
        }
    }

    /**
     * Gets duration.获取当前总时间 毫秒
     *
     * @return the duration
     */
    public long getDuration() {
        if (videoPlayer != null) {
            return videoPlayer.getDuration();
        }
        return -1;
    }

    /**
     * init EXOPlayer
     */
    private void initExoplayers(Context context) {
        if (videoPlayer == null) {
            // Create Simple ExoPlayer
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            videoSurfaceView = new PlayerView(context);

            // Bind the player to the view.
            videoSurfaceView.setUseController(false);
            videoSurfaceView.setPlayer(videoPlayer);


            //线程池
            isThread = true;
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(new WorkTask());
            exec.shutdown();

        }


        videoPlayer.prepare(getDataSource(context));

        videoPlayer.addListener(eventListener);
    }


    /**
     * The type Work task.
     */
    class WorkTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(0);
                if (isThread) {
                    continue;
                }
                return;
            }
        }
    }

    /**
     * update ui handler progress / seek / time
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (videoPlayer == null) {
                return false;
            }
            long currentPosition = videoPlayer.getCurrentPosition();
            long bufferedPosition = videoPlayer.getBufferedPosition();
            long duration = videoPlayer.getDuration();
            int progress = (int) (1000L * currentPosition / duration);
            int bprogress = (int) (1000L * bufferedPosition / duration);

            if (duration < 0) {
                return false;
            }

            String currentTime = AVUtils.stringToTime(currentPosition);
            String durationTime = AVUtils.stringToTime(duration);

            easyAudioListener.onProgress(progress, bprogress, 1000, currentTime, durationTime);
            return false;
        }
    });


    private Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {

                //缓存
                case Player.STATE_BUFFERING:
                    easyAudioListener.onBuffering();
                    break;

                //播放完毕
                case Player.STATE_ENDED:
                    easyAudioListener.onPlayEnd();
                    break;

                //准备结束
                case Player.STATE_READY:
                    easyAudioListener.onPlayReady();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            easyAudioListener.onPlayerError(error);
        }
    };


    /**
     * Gets data source.
     *
     * @return the data source
     */
    private MediaSource getDataSource(Context context) {
        DataSource.Factory dataSource = null;
        MediaSource videoSource = null;
        if (VIDEO_TYPE_DEF == playType) {
            dataSource = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "VideoPlayer"));
        } else if (VIDEO_TYPE_RTMP == playType) {
            dataSource = new RtmpDataSourceFactory();
        }
        if (VIDEO_TYPE_M3U8 == playType) {
            dataSource = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "VideoPlayer"));
        }
        if (dataSource != null) {
            if (VIDEO_TYPE_M3U8 == playType) {
                videoSource = new HlsMediaSource.Factory(dataSource).createMediaSource(Uri.parse(url));
            } else {
                videoSource = new ProgressiveMediaSource.Factory(dataSource).createMediaSource(Uri.parse(url));
            }
        }
        return videoSource;
    }


}
