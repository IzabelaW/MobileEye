package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import mobileeye.mobileeye.FavouriteNumbers.FavouriteNumber;
import mobileeye.mobileeye.FavouriteNumbers.FavouriteNumbersActivity;
import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.Navigation.NavigationActivity;
import mobileeye.mobileeye.VoiceNotes.VoiceNote;
import mobileeye.mobileeye.VoiceNotes.VoiceNotesActivity;
import mobileeye.mobileeye.database.DBHandler;
import mobileeye.mobileeye.R;

import static mobileeye.mobileeye.activity.Constants.CONFIGURATION;
import static mobileeye.mobileeye.activity.Constants.FAVOURITE_NUMBERS;
import static mobileeye.mobileeye.activity.Constants.NAVIGATION;
import static mobileeye.mobileeye.activity.Constants.OBJECT_RECOGNITION;
import static mobileeye.mobileeye.activity.Constants.VOICE_NOTES;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int SUB_ACTIVITY = 2;

    private String[] optionList = {"Notatki głosowe", "Rozpoznawanie obiektów i czytanie tekstu", "Nawigacja",  "Panel konfiguracyjny \n osoby widomej", "Ulubione numery"};

    private String[] selectedOptionInfoList = {"Wybrano opcję: notatki głosowe", "Wybrano opcję: rozpoznawanie obiektów i czytanie tekstu",
            "Wybrano opcję: nawigacja",  "Wybrano opcję: panel konfiguracyjny osoby widomej ", "Wybrano opcję: ulubione numery"};

    public static DBHandler dbHandler;


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
                default:
                    Intent voiceNotesIntent1 = new Intent(this, VoiceNotesActivity.class);
                    startActivityForResult(voiceNotesIntent1, SUB_ACTIVITY);
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
        dbHandler = new DBHandler(this);

        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, "Iza", "517105496"));
        //***********************************************************
        //Testing DataBase
        /*
       dbHandler = new DBHandler(this);
        ArrayList<VoiceNote> voiceNotesList = dbHandler.getAllVoiceNotes();
        for(int i = 0; i < voiceNotesList.size(); i++){
            Log.i("list ", Integer.toString(voiceNotesList.get(i).getId()) + " " + voiceNotesList.get(i).getTitleDirectory() +
                    " " + voiceNotesList.get(i).getContentDirectory());
        }

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
        for(int i = 1; i <= dbHandler.getFavouritePlacesCount(); i++){
            favouritePlace = dbHandler.getFavouritePlace(i);
            log2 = "Id: " + favouritePlace.getId() + ", Place name: " + favouritePlace.getPlaceName() + ", Place address: " + favouritePlace.getPlaceAddress();
            Log.d("FAVOURITE PLACE: ", log2);
        }*/
        /*

        Log.d("Inserting: ", "Inserting new voice notes...");
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Lista zakupów", "directory1"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Przypomnienie", "drectory2"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Oceny", "directory3"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount()+1, "Zadania", "directory4"));

        Log.d("Reading: ", "Reading all voice notes...");
        */

        //************************************************************
        showMenu();

    }

    private void showMenu(){
        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }
}
