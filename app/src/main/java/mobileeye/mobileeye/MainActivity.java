package mobileeye.mobileeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int VOICE_NOTES = 0;
    private static final int NAVIGATION = 1;
    private static final int OBJECT_RECOGNITION = 2;

    String[] optionList = {"Notatki głosowe", "Rozpoznawanie obiektów i czytanie tekstu", "Nawigacja"};
    String[] selectedOptionInfoList = {"Wybrano opcję: notatki głosowe", "Wybrano opcję: rozpoznawanie obiektów i czytanie tekstu",
            "Wybrano opcję: nawigacja"};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);
                Toast.makeText(this, "Selected " + selectedOption, Toast.LENGTH_LONG).show();
            }

            switch (selectedOption) {
                case VOICE_NOTES:
                    Intent voiceNotesIntent = new Intent(this, VoiceNotesActivity.class);
                    startActivity(voiceNotesIntent);
                case NAVIGATION:
                    Intent navigationIntent = new Intent(this, NavigationActivity.class);
                    startActivity(navigationIntent);
                case OBJECT_RECOGNITION:
                    Intent objectRecognitionIntent = new Intent(this, ObjectRecognitionActivity.class);
                    startActivity(objectRecognitionIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }
}
