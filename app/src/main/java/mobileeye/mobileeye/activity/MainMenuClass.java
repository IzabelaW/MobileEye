package mobileeye.mobileeye.activity;

import android.content.Intent;

/**
 * Created by Nexito on 20.11.2017.
 */

public class MainMenuClass implements OptionsSelection{

    private static final int VOICE_NOTES = 0;
    private static final int OBJECT_RECOGNITION = 1;
    private static final int NAVIGATION = 2;

    private String[] optionList = {"Notatki głosowe", "Rozpoznawanie obiektów i czytanie tekstu", "Nawigacja"};

    private String[] selectedOptionInfoList = {"Wybrano opcję: notatki głosowe", "Wybrano opcję: rozpoznawanie obiektów i czytanie tekstu",
            "Wybrano opcję: nawigacja"};
    private VoiceNotesClass voiceNotesClass;

    public MainMenuClass(){

        //logic classes initialization
        voiceNotesClass=new VoiceNotesClass();

    }
    @Override
    public void optionSelected(int currentOption){
        switch (currentOption) {
            case VOICE_NOTES:
                voiceNotesClass.initialize();
                break;
            case NAVIGATION:

                break;
            case OBJECT_RECOGNITION:

                break;
            default:
                voiceNotesClass.initialize();
                break;
        }

    }

    @Override
    public void initialize() {
        OptionsClass.initializeOptions(optionList,selectedOptionInfoList,this);

    }
}
