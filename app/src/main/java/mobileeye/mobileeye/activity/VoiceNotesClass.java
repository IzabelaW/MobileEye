package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mobileeye.mobileeye.R;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class VoiceNotesClass implements OptionsSelection{

    private static final int ADD_VOICENOTE = 0;
    private static final int DELETE_VOICENOTE = 1;
    private static final int LISTEN_VOICENOTE = 2;

    private String[] optionList = {"Dodaj nową notatkę", "Usuń istniejącą notatkę", "Przeglądaj swoje notatki"};
    private String[] selectedOptionInfoList = {"Wybrano opcję: dodaj nową notatkę", "Wybrano opcję: usuń istniejącą notatkę",
            "Wybrano opcję: przeglądaj swoje notatki"};

    @Override
    public void initialize() {
        OptionsClass.initializeOptions(optionList, selectedOptionInfoList, this);
    }

   public void optionSelected(int selectedOption) {

        switch (selectedOption) {
            case ADD_VOICENOTE:
                break;
            case DELETE_VOICENOTE:
                break;
            case LISTEN_VOICENOTE:
                break;
            default:
                break;
        }
    }




}
