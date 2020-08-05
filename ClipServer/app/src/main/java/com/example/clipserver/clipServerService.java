package com.example.clipserver;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class clipServerService extends Service {

    @SuppressWarnings("unused")
    private final String TAG = "MusicService";
    private MediaPlayer mPlayer;
    private  int songPausedIndex = 0;
    private int currSong = 0;

    private final int songs[] = {0,R.raw.mountainking,R.raw.borodin,R.raw.figaro,R.raw.shubert,R.raw.thelark};


    @Override
    public void onCreate() {
        super.onCreate();

        // Set up the Media Player
        mPlayer = MediaPlayer.create(this, songs[1]);
    }

    @Override
    public void onDestroy() {

        if (null != mPlayer) {

            mPlayer.stop();
            mPlayer.release();

        }
    }

    private final musicServiceInterface.Stub msi = new musicServiceInterface.Stub() {

        public void play(int song) {
            if(mPlayer.isPlaying()){
                mPlayer.stop();
            }
            //mPlayer.selectTrack(songs[song]);
            mPlayer = MediaPlayer.create(getApplicationContext(), songs[song]);
            currSong = song;
            mPlayer.start();

        }

        public void pause() {
            songPausedIndex = mPlayer.getCurrentPosition();
            mPlayer.pause();
        }

        public void resume() {
            mPlayer.seekTo(songPausedIndex);
            mPlayer.start();
        }

        public void stop() {
            mPlayer.stop();
            songPausedIndex = 0;
        }
    };
    // Can't bind to this Service
    @Override
    public IBinder onBind(Intent intent) {
        return msi;

    }
}

