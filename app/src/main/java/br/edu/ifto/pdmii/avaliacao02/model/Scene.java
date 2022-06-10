package br.edu.ifto.pdmii.avaliacao02.model;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Scene implements Parcelable {
    private final int resource;
    private final Long playbackStart;
    private final Long playbackStop;
    private CountDownTimer timer;

    public Scene(int resource, Long playbackStart, Long playbackStop) {
        this.resource = resource;
        this.playbackStart = playbackStart;
        this.playbackStop = playbackStop;
    }

    public Scene(int resource, Long playbackStart) {
        this(resource, playbackStart, null);
    }

    public Scene(int resource) {
        this(resource, 0L, null);
    }

    protected Scene(Parcel in) {
        resource = in.readInt();
        if (in.readByte() == 0) {
            playbackStart = null;
        } else {
            playbackStart = in.readLong();
        }
        if (in.readByte() == 0) {
            playbackStop = null;
        } else {
            playbackStop = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resource);
        if (playbackStart == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(playbackStart);
        }
        if (playbackStop == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(playbackStop);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Scene> CREATOR = new Creator<Scene>() {
        @Override
        public Scene createFromParcel(Parcel in) {
            return new Scene(in);
        }

        @Override
        public Scene[] newArray(int size) {
            return new Scene[size];
        }
    };

    public int getResource() {
        return resource;
    }

    public Long getPlaybackStart() {
        return playbackStart;
    }

    public Long getPlaybackStop() {
        return playbackStop;
    }

    public void play(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(Math.toIntExact(playbackStart));

        if (playbackStop == null) {
            mediaPlayer.start();
            return;
        }
        if (playbackStop < playbackStart) {
            throw new IllegalArgumentException("O intervalo de reprodução deve ser maior que zero.");
        }

        timer = new CountDownTimer(playbackStop - playbackStart, 1000) {
            @Override
            public void onTick(long l) {
                mediaPlayer.start();
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
            }
        }.start();
    }

    public void stop(MediaPlayer mediaPlayer) {
        if (timer != null) timer.cancel();
        if (mediaPlayer != null) mediaPlayer.stop();
    }
}
