package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.VoiceNote;
import mobileeye.mobileeye.database.DBHandler;
import mobileeye.mobileeye.FavouriteNumber;
import mobileeye.mobileeye.R;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int VOICE_NOTES = 0;
    private static final int OBJECT_RECOGNITION = 1;
    private static final int NAVIGATION = 2;

    private String[] optionList = {"Notatki głosowe", "Rozpoznawanie obiektów i czytanie tekstu", "Nawigacja"};

    private String[] selectedOptionInfoList = {"Wybrano opcję: notatki głosowe", "Wybrano opcję: rozpoznawanie obiektów i czytanie tekstu",
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
                    break;
                case OBJECT_RECOGNITION:
                    Intent objectRecognitionIntent = new Intent(this, ObjectRecognitionActivity.class);
                    startActivity(objectRecognitionIntent);
                    break;
                case NAVIGATION:
                    Intent navigationIntent = new Intent(this, NavigationActivity.class);
                    startActivity(navigationIntent);
                    break;
                default:
                    Intent voiceNotesIntent1 = new Intent(this, VoiceNotesActivity.class);
                    startActivity(voiceNotesIntent1);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //***********************************************************
        //Testing DataBase
        DBHandler dbHandler = new DBHandler(this);

        //Inserting FavouriteNumbers
        Log.d("Inserting: ", "Inserting new numbers...");
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, "Tata", "600336250"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, "Mama", "662686854"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, "Iza", "517105496"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, "Paweł", "660345404"));

        Log.d("Reading: ", "Reading all numbers...");
        String log1;
        FavouriteNumber favouriteNumber;
        for(int i = 1; i <= dbHandler.getFavouriteNumbersCount(); i++){
            favouriteNumber = dbHandler.getFavouriteNumber(i);
            log1 = "Id: " + favouriteNumber.getId() + ", Name: " + favouriteNumber.getContactName() + ", Number: " + favouriteNumber.getContactNumber();
            Log.d("FAVOURITE NUMBER: ", log1);
        }

        Log.d("Inserting: ", "Inserting new places...");
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, "Dom", "Brenica 85"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, "Szkoła", "ul. Łódzka 18"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, "Sklep spożywczy", "ul.Tomaszowska 1"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, "Kościół", "ul. Tomaszowska 3"));

        Log.d("Reading: ", "Reading all numbers...");
        String log2;
        FavouritePlace favouritePlace;
        for(int i = 1; i <= 0; i++){
            favouritePlace = dbHandler.getFavouritePlace(i);
            log2 = "Id: " + favouritePlace.getId() + ", Place name: " + favouritePlace.getPlaceName() + ", Place address: " + favouritePlace.getPlaceAddress();
            Log.d("FAVOURITE PLACE: ", log2);
        }

        Log.d("Inserting: ", "Inserting new voice notes...");
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Lista zakupów", "directory1"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Przypomnienie", "drectory2"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Oceny", "directory3"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Zadania", "directory4"));

        Log.d("Reading: ", "Reading all voice notes...");
        String log3;
        VoiceNote voiceNote;
        for(int i = 1; i <= 0; i++){
            voiceNote = dbHandler.getVoiceNote(i);
            log3 = "Id: " + voiceNote.getId() + ", Title: " + voiceNote.getTitle() + ", Directory: " + voiceNote.getDirectory();
            Log.d("VOICE NOTE: ", log3);
        }
        //************************************************************

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }
}
