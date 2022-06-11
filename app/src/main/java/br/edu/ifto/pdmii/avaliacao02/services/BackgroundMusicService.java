package br.edu.ifto.pdmii.avaliacao02.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import br.edu.ifto.pdmii.avaliacao02.model.Scene;

public class BackgroundMusicService extends Service {
    public static final String START_PLAYBACK_ACTION = "MEDIA_START";
    public static final String STOP_PLAYBACK_ACTION = "MEDIA_STOP";

    private static MediaPlayer mediaPlayer;
    private static Scene scene;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(START_PLAYBACK_ACTION)) {
            scene = intent.getParcelableExtra("BACKGROUND_SCENE");
            mediaPlayer = MediaPlayer.create(this, scene.getResource());

            scene.play(mediaPlayer);
        } else if (intent.getAction().equals(STOP_PLAYBACK_ACTION)) {
            scene.stop(mediaPlayer);
            mediaPlayer = null;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
