package com.pep.core.libav;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


import com.google.android.exoplayer2.ExoPlayerFactory;
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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.view.KeyEvent.KEYCODE_BACK;
import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_M3U8;
import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_DEF;
import static com.pep.core.libav.EasyAvType.VIDEO_TYPE_RTMP;


/**
 * The type Pep video fragment.
 */
public class EasyVideoFragment extends DialogFragment {



    /**
     * Current video play type
     */
    private int playType = -1;

    /**
     * is Play
     */
    private boolean playWhenReady;

    /**
     * is update time handler thread run
     */
    private boolean isThread = true;



    /**
     * video url
     */
    private String url;

    /**
     * is buttom show/hide
     */
    private boolean isButtomShow = true;

    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;
    private Context context;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private TextView tvPlayerPosition, tvPlayerDuration;
    private ImageView btnPlay, playFull, icClose;
    private View contentView, viewHolder, layoutProgress, llButtom, viewBlack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreenBlack);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_video, container, false);
        this.context = getContext();
        initView();
        initData();
        initExoplayers();
        initListener();
        return contentView;
    }


    /**
     * init listener
     */
    private void initListener() {
        seekBar.setOnSeekBarChangeListener(mSeekListener);
        videoPlayer.addListener(playbackParameters);
        btnPlay.setOnClickListener(playClickListener);
        icClose.setOnClickListener(closeClickListener);
        playFull.setOnClickListener(playFullClickListener);
        Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);
        viewBlack.setOnClickListener(viewBlackListener);
    }

    /**
     * init views
     */
    private void initView() {
        videoSurfaceView = contentView.findViewById(R.id.video_view);
        progressBar = contentView.findViewById(R.id.progress);
        viewBlack = contentView.findViewById(R.id.view_black);
        seekBar = contentView.findViewById(R.id.seek_bar);
        tvPlayerPosition = contentView.findViewById(R.id.tv_player_position);
        tvPlayerDuration = contentView.findViewById(R.id.tv_player_duration);
        btnPlay = contentView.findViewById(R.id.play_btn);
        playFull = contentView.findViewById(R.id.play_full);
        icClose = contentView.findViewById(R.id.iv_close);
        viewHolder = contentView.findViewById(R.id.view_holder);
        layoutProgress = contentView.findViewById(R.id.layout_progress);
        llButtom = contentView.findViewById(R.id.ll_buttom);
    }

    /**
     * init data
     */
    private void initData() {
        //屏幕常亮
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //线程池
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new WorkTask());
        exec.shutdown();
        //根据视频类型更新对应ui
        videoTypeUIUpdate();
    }

    /**
     * video type update ui
     */
    private void videoTypeUIUpdate() {
        llButtom.animate().translationY(200);
        switch (playType) {
            case VIDEO_TYPE_DEF:
                viewHolder.setVisibility(View.GONE);
                layoutProgress.setVisibility(View.VISIBLE);
                break;
            case VIDEO_TYPE_RTMP:
                viewHolder.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.GONE);
                break;
            default:

                break;
        }
    }

    /**
     * Get is Full
     */
    private boolean isFullScreen() {
        return ((Activity) Objects.requireNonNull(getContext())).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    /**
     * full screen
     */
    private void toggleFullScreen() {
        if (isFullScreen()) {
            ((Activity) Objects.requireNonNull(getContext())).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            playFull.setImageResource(R.drawable.ic_media_fullscreen_stretch);
            icClose.setVisibility(View.VISIBLE);
        } else {
            ((Activity) Objects.requireNonNull(getContext())).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            playFull.setImageResource(R.drawable.ic_media_fullscreen_shrink);
            icClose.setVisibility(View.GONE);
        }
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
            long currentPosition = videoPlayer.getCurrentPosition();
            long bufferedPosition = videoPlayer.getBufferedPosition();
            long duration = videoPlayer.getDuration();

            long position = 1000L * currentPosition / duration;
            long bposition = 1000L * bufferedPosition / duration;

            seekBar.setProgress((int) position);
            seekBar.setSecondaryProgress((int) bposition);


            if (duration < 0) {
                return false;
            }

            tvPlayerPosition.setText(AVUtils.stringToTime(currentPosition));
            tvPlayerDuration.setText(AVUtils.stringToTime(duration));
            seekBar.setMax(1000);
            return false;
        }
    });


    /**
     * init EXOPlayer
     */
    private void initExoplayers() {
        // Create Simple ExoPlayer
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // Bind the player to the view.
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        videoPlayer.prepare(getDataSource());
        videoPlayer.setPlayWhenReady(true);
    }


    /**
     * The Dialog interface.
     */
    private DialogInterface.OnKeyListener dialogInterface = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            if (KEYCODE_BACK == i) {
                if (isFullScreen()) {
                    ((Activity) Objects.requireNonNull(getContext())).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    playFull.setImageResource(R.drawable.ic_media_fullscreen_stretch);
                    icClose.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
            return false;
        }
    };


    /**
     * Sets data.
     *
     * @param playType the play type
     * @param url      the url
     */
    public void setData(int playType, String url) {
        EasyVideoFragment.this.playType = playType;
        EasyVideoFragment.this.url = url;

    }

    /**
     * Gets data source.
     *
     * @return the data source
     */
    private MediaSource getDataSource() {
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




    /**
     * play Listener
     */
    private View.OnClickListener playClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (playWhenReady) {
                videoPlayer.setPlayWhenReady(false);
                btnPlay.setImageResource(R.drawable.ic_seek_play);
            } else {
                videoPlayer.setPlayWhenReady(true);
                btnPlay.setImageResource(R.drawable.ic_media_stop);
            }
        }
    };

    /**
     * view Listener
     */
    private View.OnClickListener viewBlackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isButtomShow) {
                llButtom.animate().translationY(200);
            } else {
                llButtom.animate().translationY(0);
            }
            isButtomShow = !isButtomShow;
        }
    };

    /**
     * playFull Listener
     */
    private View.OnClickListener playFullClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggleFullScreen();

        }
    };

    /**
     * close Listener
     */
    private View.OnClickListener closeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    /**
     * seek Listener
     */
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {

        }
        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = videoPlayer.getDuration();
            long newPosition = (duration * progress) / 1000L;
            videoPlayer.seekTo((int) newPosition);
        }
        @Override
        public void onStopTrackingTouch(SeekBar bar) {
        }
    };

    /**
     * play Listener
     */
    private Player.EventListener playbackParameters = new Player.EventListener() {


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            EasyVideoFragment.this.playWhenReady = playWhenReady;
            switch (playbackState) {

                //缓存
                case Player.STATE_BUFFERING:
                    if (progressBar != null) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    break;

                //播放完毕
                case Player.STATE_ENDED:
                    videoPlayer.seekTo(0);
                    videoPlayer.setPlayWhenReady(false);
                    btnPlay.setImageResource(R.drawable.ic_seek_play);
                    break;

                //准备结束
                case Player.STATE_READY:
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message message) {
                            progressBar.setVisibility(View.GONE);
                            viewBlack.animate().alpha(0);
                            llButtom.setVisibility(View.VISIBLE);

                            llButtom.animate().translationY(0);
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0, 500);
                    break;
                default:
                    break;
            }
        }


    };




    @Override
    public void onResume() {
        super.onResume();
        if (!playWhenReady) {
            videoPlayer.setPlayWhenReady(true);
            btnPlay.setImageResource(R.drawable.ic_media_stop);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayer.release();
        isThread = false;
    }

}
