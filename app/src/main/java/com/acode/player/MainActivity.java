package com.acode.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.acode.player.bean.PlayerBean;
import com.acode.player.data.Data;
import com.acode.player.lib.entity.Material;
import com.acode.player.lib.files.VideosFilsUtils;
import com.acode.player.lib.utils.PermissionUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private final int PERMISSION_REQ_CODE = 100;
    //获取本地视频列表
    private Button btn;

    private Button btn_intent;

    private LinearLayout ll_video_files_root;

    private PermissionUtils permissionUtils;

    private AcodePlayerView acodePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("post", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        permissionUtils = new PermissionUtils(this);
        permissionUtils.requestPermission(PERMISSION_REQ_CODE, permissionUtils.request_permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_CODE:

                break;
        }
    }

    private void initView() {
        acodePlayerView = findViewById(R.id.acodePlayerView);
        btn = findViewById(R.id.btn);
        btn_intent = findViewById(R.id.btn_intent);
        ll_video_files_root = findViewById(R.id.ll_video_files_root);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_video_files_root.removeAllViews();
                for (final PlayerBean playerBean : Data.getPlayerBeans()) {
                    Button button = new Button(MainActivity.this);
                    button.setText(playerBean.getTitle());
                    ll_video_files_root.addView(button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acodePlayerView.pausePlayer();
                            acodePlayerView.readyPlayer(playerBean);
                        }
                    });
                }
            }
        });
        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TestActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("post", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        acodePlayerView.onResume();
        Log.d("post", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        acodePlayerView.onPause();
        Log.d("post", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("post", "onStop()");
    }

    @Override
    protected void onDestroy() {
        if (acodePlayerView == null) {
            return;
        }
        acodePlayerView.cancel();
        super.onDestroy();
    }

    /**
     * 解决找不到某些jar的静态方法
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
