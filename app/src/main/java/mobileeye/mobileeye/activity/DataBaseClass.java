package mobileeye.mobileeye.activity;

import android.content.Context;
import android.util.Log;

import mobileeye.mobileeye.FavouriteNumber;
import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.VoiceNote;
import mobileeye.mobileeye.database.DBHandler;

/**
 * Created by Nexito on 20.11.2017.
 */

public class DataBaseClass {

    private Context context;

    public DataBaseClass(Context context){
        this.context=context;

    }

    public void initializeDataBase(){

        //Testing DataBase
        DBHandler dbHandler = new DBHandler(context);

        //Inserting FavouriteNumbers
        Log.d("Inserting: ", "Inserting new numbers...");
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount() + 1, "Tata", "600336250"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount() + 1, "Mama", "662686854"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount() + 1, "Iza", "517105496"));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount() + 1, "Paweł", "660345404"));

        Log.d("Reading: ", "Reading all numbers...");
        String log1;
        FavouriteNumber favouriteNumber;
        for (int i = 1; i <= dbHandler.getFavouriteNumbersCount(); i++) {
            favouriteNumber = dbHandler.getFavouriteNumber(i);
            log1 = "Id: " + favouriteNumber.getId() + ", Name: " + favouriteNumber.getContactName() + ", Number: " + favouriteNumber.getContactNumber();
            Log.d("FAVOURITE NUMBER: ", log1);
        }

        Log.d("Inserting: ", "Inserting new places...");
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount() + 1, "Dom", "Brenica 85"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount() + 1, "Szkoła", "ul. Łódzka 18"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount() + 1, "Sklep spożywczy", "ul.Tomaszowska 1"));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount() + 1, "Kościół", "ul. Tomaszowska 3"));

        Log.d("Reading: ", "Reading all numbers...");
        String log2;
        FavouritePlace favouritePlace;
        for (int i = 1; i <= dbHandler.getFavouritePlacesCount(); i++) {
            favouritePlace = dbHandler.getFavouritePlace(i);
            log2 = "Id: " + favouritePlace.getId() + ", Place name: " + favouritePlace.getPlaceName() + ", Place address: " + favouritePlace.getPlaceAddress();
            Log.d("FAVOURITE PLACE: ", log2);
        }

        Log.d("Inserting: ", "Inserting new voice notes...");
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount() + 1, "Lista zakupów", "directory1"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount() + 1, "Przypomnienie", "drectory2"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount() + 1, "Oceny", "directory3"));
        dbHandler.addNewVoiceNote(new VoiceNote(dbHandler.getVoiceNotesCount() + 1, "Zadania", "directory4"));

        Log.d("Reading: ", "Reading all voice notes...");
        String log3;
        VoiceNote voiceNote;
        for (int i = 1; i <= dbHandler.getVoiceNotesCount(); i++) {
            voiceNote = dbHandler.getVoiceNote(i);
            log3 = "Id: " + voiceNote.getId() + ", Title: " + voiceNote.getTitleDirectory() + ", Directory: " + voiceNote.getContentDirectory();
            Log.d("VOICE NOTE: ", log3);
        }
    }
}
