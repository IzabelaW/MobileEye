package mobileeye.mobileeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class VoiceNotesActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int ADD_VOICENOTE = 0;
    private static final int DELETE_VOICENOTE = 1;
    private static final int LISTEN_VOICENOTE = 2;

    private String[] optionList = {"Dodaj nową notatkę", "Usuń istniejącą notatkę", "Przeglądaj swoje notatki"};
    private String[] selectedOptionInfoList = {"Wybrano opcję: dodaj nową notatkę", "Wybrano opcję: usuń istniejącą notatkę",
            "Wybrano opcję: przeglądaj swoje notatki"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_notes);

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
                Toast.makeText(this, "Selected " + selectedOption, Toast.LENGTH_LONG).show();
            }

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
}
