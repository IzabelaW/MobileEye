package mobileeye.mobileeye.VoiceNotes;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kasia on 2017-11-13.
 */

public class Recorder {
    private MediaRecorder mRecorder = null;


    public void startRecording(String outputFile) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(outputFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.d("REC", "prepare() failed");
        }
            mRecorder.start();
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }






}
