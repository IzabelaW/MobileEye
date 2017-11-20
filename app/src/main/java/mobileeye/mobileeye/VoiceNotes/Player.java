package mobileeye.mobileeye.VoiceNotes;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kasia on 2017-11-17.
 */

public class Player {
    private MediaPlayer mPlayer = null;


    public void startPlaying(String fileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("START_PLAYING", "prepare() failed");
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

}
