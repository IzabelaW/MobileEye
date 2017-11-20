package mobileeye.mobileeye.VoiceNotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import mobileeye.mobileeye.R;
import mobileeye.mobileeye.activity.MenuActivity;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class VoiceNotesActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";


    private static final int OPTION_MENU_RESULT = 0;
    private static final int SUB_ACTIVITY = 1;

    private static final int ADD_VOICENOTE = 0;
    private static final int DELETE_VOICENOTE = 1;
    private static final int LISTEN_VOICENOTE = 2;
    private static final int CANCEL = 3;

    private String[] optionList = {"Dodaj nową notatkę", "Usuń istniejącą notatkę", "Przeglądaj swoje notatki"};
    private String[] selectedOptionInfoList = {"Wybrano opcję: dodaj nową notatkę", "Wybrano opcję: usuń istniejącą notatkę",
            "Wybrano opcję: przeglądaj swoje notatki"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_notes);
        TextView infoTextView = (TextView) findViewById(R.id.voiceNotesInfoTextView);
        showMenu();
    }



    private void showMenu(){
        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);
                switch (selectedOption) {
                    case ADD_VOICENOTE:
                        Intent addVoiceNoteIntent = new Intent(this, AddingNewNoteActivity.class);
                        startActivityForResult(addVoiceNoteIntent, SUB_ACTIVITY);
                        break;
                    case DELETE_VOICENOTE:
                        Intent deleteVoiceNoteIntent = new Intent(this, DeletingNoteActivity.class);
                        startActivityForResult(deleteVoiceNoteIntent, SUB_ACTIVITY);
                        break;
                    case LISTEN_VOICENOTE:
                        Intent listenVoiceNoteIntent = new Intent(this, ListeningNoteActivity.class);
                        startActivityForResult(listenVoiceNoteIntent, SUB_ACTIVITY);
                        break;
                    case CANCEL:
                        Intent d = new Intent();
                        d.putExtra("selectedOption", CANCEL);
                        setResult(RESULT_OK, data);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }

        else if (requestCode == SUB_ACTIVITY){
            showMenu();
        }

    }



}
