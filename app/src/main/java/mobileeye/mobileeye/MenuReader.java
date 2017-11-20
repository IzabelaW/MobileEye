package mobileeye.mobileeye;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Tomasz on 02.11.2017.
 */

public class MenuReader implements TextToSpeech.OnInitListener{

    TextToSpeech tts;
    ReaderListener readerListener;
    HashMap<String, String> map = new HashMap<String, String>();

    public MenuReader(Context context){
        tts = new TextToSpeech(context, this);
    }

    public void read(String option){
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts.speak(option, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void read(String option, ReaderListener readerListener){
        this.readerListener = readerListener;
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
        tts.speak(option, TextToSpeech.QUEUE_FLUSH, map);
    }



    public void setReaderListener(ReaderListener readerListener){
        this.readerListener = readerListener;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(new Locale("pl", "PL"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported!");
            }

            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onDone(String utteranceId) {
                    Log.i("compl ", "reader");
                    if(readerListener != null) {
                        readerListener.onReadingCompleted();
                    }
                }

                @Override
                public void onError(String utteranceId) {
                }

                @Override
                public void onStart(String utteranceId) {
                }
            });
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

}
