package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mobileeye.mobileeye.FavouriteNumbers.FavouriteNumber;
import mobileeye.mobileeye.FavouriteNumbers.FavouriteNumbersActivity;
import mobileeye.mobileeye.Navigation.NavigationActivity;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.VoiceNotes.VoiceNotesActivity;
import mobileeye.mobileeye.database.DBHandler;

import static mobileeye.mobileeye.activity.Constants.CONFIGURATION;
import static mobileeye.mobileeye.activity.Constants.FAVOURITE_NUMBERS;
import static mobileeye.mobileeye.activity.Constants.NAVIGATION;
import static mobileeye.mobileeye.activity.Constants.OBJECT_RECOGNITION;
import static mobileeye.mobileeye.activity.Constants.VOICE_NOTES;
import static mobileeye.mobileeye.activity.Constants.GUIDE;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int SUB_ACTIVITY = 2;

    private String[] optionList = {
            "Notatki głosowe",
            "Rozpoznawanie obiektów i czytanie tekstu",
            "Nawigacja",
            "Panel konfiguracyjny\n osoby widomej",
            "Ulubione numery",
            "Tryb przewodnika"};

    private String[] selectedOptionInfoList = {
            "Wybrano opcję: notatki głosowe",
            "Wybrano opcję: rozpoznawanie obiektów i czytanie tekstu",
            "Wybrano opcję: nawigacja",
            "Wybrano opcję: panel konfiguracyjny osoby widomej",
            "Wybrano opcję: ulubione numery",
            "Wybrano opcję: tryb przewodnika"};

    public static DBHandler dbHandler;

    public static Intent starterIntent;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);
            }

            switch (selectedOption) {
                case VOICE_NOTES:
                    Intent voiceNotesIntent = new Intent(this, VoiceNotesActivity.class);
                    startActivityForResult(voiceNotesIntent, SUB_ACTIVITY);
                    break;
                case NAVIGATION:
                    Intent navigationIntent = new Intent(this, NavigationActivity.class);
                    startActivityForResult(navigationIntent, SUB_ACTIVITY);
                    break;
                case OBJECT_RECOGNITION:
                    Intent objectRecognitionIntent = new Intent(this, ObjectRecognitionActivity.class);
                    startActivityForResult(objectRecognitionIntent, SUB_ACTIVITY);
                    break;
                case CONFIGURATION:
                    Intent configurationIntent = new Intent(this, ConfigurationActivity.class);
                    startActivityForResult(configurationIntent,SUB_ACTIVITY);
                    break;
                case FAVOURITE_NUMBERS:
                    Intent favouriteNumbersIntent = new Intent(this, FavouriteNumbersActivity.class);
                    startActivityForResult(favouriteNumbersIntent, SUB_ACTIVITY);
                    break;
                case GUIDE:
                    Intent guideIntent = new Intent(this, GuideActivity.class);
                    startActivityForResult(guideIntent, SUB_ACTIVITY);
                    break;
                default:
//                    Intent voiceNotesIntent1 = new Intent(this, VoiceNotesActivity.class);
//                    startActivityForResult(voiceNotesIntent1, SUB_ACTIVITY);
                    finish();
                    break;
            }
        }

        else if (requestCode == SUB_ACTIVITY){
            showMenu();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starterIntent = getIntent();
        dbHandler = new DBHandler(this);
        showMenu();

    }

    private void showMenu(){
        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }
}
