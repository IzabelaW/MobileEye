package mobileeye.mobileeye;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import mobileeye.mobileeye.activity.MenuActivity;

/**
 * Created by Tomasz on 02.11.2017.
 */

public class MenuDisplayer  implements TextToSpeech.OnInitListener {

    String[] optionList;
    TextToSpeech tts;

    public MenuDisplayer(String[] optionList) {

        this.optionList = optionList;
    }
    public MenuDisplayer(Context context){
        tts = new TextToSpeech(context, this);
    }

    public void display(String option){
        tts.speak(option, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void show(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra("optionList", optionList);
        context.startActivity(intent);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("pl", "PL"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported!");
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }
}
