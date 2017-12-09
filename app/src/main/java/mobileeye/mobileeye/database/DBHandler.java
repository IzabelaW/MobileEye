package mobileeye.mobileeye.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mobileeye.mobileeye.FavouriteNumber;
import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.VoiceNotes.VoiceNote;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "MobileEyeDB";

    // Voice notes table name
    private static final String TABLE_VOICE_NOTES = "VoiceNotes";
    //Voice notes table columns names
    private static final String VOICENOTE_KEY_ID = "id";
    private static final String VOICENOTE_KEY_TITLE_DIRECTORY = "titleDirectory";
    private static final String VOICENOTE_KEY_CONTENT_DIRECTORY = "contentDirectory";

    // Favourite numbers table name
    private static final String TABLE_FAVOURITE_NUMBERS = "FavouriteNumbers";
    // Favourite numbers table columns names
    private static final String FAV_NUMBER_KEY_ID = "id";
    private static final String FAV_NUMBER_KEY_CONTACTNAME = "contactName";
    private static final String FAV_NUMBER_KEY_CONTACTNUMBER = "contactNumber";

    // Favourite places table name
    private static final String TABLE_FAVOURITE_PLACES = "FavouritePlaces";
    // Favourite places table columns names
    private static final String FAV_PLACE_KEY_ID = "id";
    private static final String FAV_PLACE_KEY_PLACENAME = "placeName";
    private static final String FAV_PLACE_KEY_PLACEADDRESS = "placeAddress";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VOICENOTES_TABLE = "CREATE TABLE " + TABLE_VOICE_NOTES + " ( " + VOICENOTE_KEY_ID + " INTEGER, " + VOICENOTE_KEY_TITLE_DIRECTORY + " TEXT, " + VOICENOTE_KEY_CONTENT_DIRECTORY + " TEXT " + ")";
        String CREATE_FAVOURITENUMBERS_TABLE = "CREATE TABLE " + TABLE_FAVOURITE_NUMBERS + " ( " + FAV_NUMBER_KEY_ID + " INTEGER, " + FAV_NUMBER_KEY_CONTACTNAME + " TEXT, " + FAV_NUMBER_KEY_CONTACTNUMBER + " TEXT " + ")";
        String CREATE_FAVOURITEPLACES_TABLE = "CREATE TABLE " + TABLE_FAVOURITE_PLACES + " ( " + FAV_PLACE_KEY_ID + " INTEGER, " + FAV_PLACE_KEY_PLACENAME + " TEXT, " + FAV_PLACE_KEY_PLACEADDRESS + " TEXT " + ")";

        sqLiteDatabase.execSQL(CREATE_VOICENOTES_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVOURITENUMBERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVOURITEPLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed 
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VOICE_NOTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_NUMBERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_PLACES);
        // Creating tables again 
        onCreate(sqLiteDatabase);
    }

    public void addNewVoiceNote(VoiceNote voiceNote) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VOICENOTE_KEY_ID, voiceNote.getId());
        values.put(VOICENOTE_KEY_TITLE_DIRECTORY, voiceNote.getTitleDirectory()); // Voice note title 
        values.put(VOICENOTE_KEY_CONTENT_DIRECTORY, voiceNote.getContentDirectory()); // Voice note content 
        // inserting a row 
        sqLiteDatabase.insert(TABLE_VOICE_NOTES, null, values);
        sqLiteDatabase.close(); // Closing database connection 
    }

    public VoiceNote getVoiceNote(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_VOICE_NOTES, new String[]{VOICENOTE_KEY_ID, VOICENOTE_KEY_TITLE_DIRECTORY, VOICENOTE_KEY_CONTENT_DIRECTORY}, VOICENOTE_KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        VoiceNote voiceNote = new VoiceNote(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return voiceNote;
    }

    public void deleteVoiceNote(VoiceNote voiceNote) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_VOICE_NOTES, VOICENOTE_KEY_ID + "=?", new String[]{String.valueOf(voiceNote.getId())});
        sqLiteDatabase.execSQL("UPDATE " + TABLE_VOICE_NOTES + " SET id = id - 1" + " WHERE id > " + voiceNote.getId());
        sqLiteDatabase.close();
    }

    public void deleteAllVoiceNotes() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE * FROM " + TABLE_VOICE_NOTES);
        sqLiteDatabase.close();
    }

    public ArrayList<VoiceNote> getAllVoiceNotes() {
        ArrayList<VoiceNote> voiceNotesList = new ArrayList<VoiceNote>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_VOICE_NOTES;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VoiceNote voiceNote = new VoiceNote(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
                // Adding voice note to list
                voiceNotesList.add(voiceNote);
            } while (cursor.moveToNext());
        }
        // return voice notes list
        return voiceNotesList;
    }

    public int getVoiceNotesCount() {
        int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_VOICE_NOTES;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        // return count
        return count;
    }

    public void addNewFavouriteNumber(FavouriteNumber favouriteNumber) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV_NUMBER_KEY_ID, favouriteNumber.getId());
        values.put(FAV_NUMBER_KEY_CONTACTNAME, favouriteNumber.getContactName()); // Favourite contact name
        values.put(FAV_NUMBER_KEY_CONTACTNUMBER, favouriteNumber.getContactNumber()); // Favourite contact phone number
        // inserting a row 
        sqLiteDatabase.insert(TABLE_FAVOURITE_NUMBERS, null, values);
        sqLiteDatabase.close(); // Closing database connection 
    }

    public FavouriteNumber getFavouriteNumber(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_FAVOURITE_NUMBERS, new String[]{FAV_NUMBER_KEY_ID, FAV_NUMBER_KEY_CONTACTNAME, FAV_NUMBER_KEY_CONTACTNUMBER}, FAV_NUMBER_KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        FavouriteNumber favouriteNumber = new FavouriteNumber(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return favouriteNumber;
    }

    public void deleteFavouriteNumber(FavouriteNumber favouriteNumber) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_FAVOURITE_NUMBERS, FAV_NUMBER_KEY_ID + "=?", new String[]{String.valueOf(favouriteNumber.getId())});
        sqLiteDatabase.execSQL("UPDATE " + TABLE_FAVOURITE_NUMBERS + " SET id = id - 1" + " WHERE id > " + favouriteNumber.getId());
        sqLiteDatabase.close();
    }

    public void deleteAllFavouriteNumbers() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE * FROM " + TABLE_FAVOURITE_NUMBERS);
        sqLiteDatabase.close();
    }

    public ArrayList<FavouriteNumber> getAllFavouriteNumbers() {
        ArrayList<FavouriteNumber> favouriteNumberList = new ArrayList<FavouriteNumber>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITE_NUMBERS;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavouriteNumber favouriteNumber = new FavouriteNumber(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
                // Adding favourite number to list
                favouriteNumberList.add(favouriteNumber);
            } while (cursor.moveToNext());
        }
        // return favourite numbers list
        return favouriteNumberList;
    }

    public int getFavouriteNumbersCount() {
        int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_FAVOURITE_NUMBERS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        // return count
        return count;
    }

    public void addNewFavouritePlace(FavouritePlace favouritePlace) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV_PLACE_KEY_ID, favouritePlace.getId());
        values.put(FAV_PLACE_KEY_PLACENAME, favouritePlace.getPlaceName()); // Favourite place name
        values.put(FAV_PLACE_KEY_PLACEADDRESS, favouritePlace.getPlaceAddress()); // Favourite place address
        // Inserting Row 
        sqLiteDatabase.insert(TABLE_FAVOURITE_PLACES, null, values);
        sqLiteDatabase.close(); // Closing database connection 
    }

    //id starts at 1
    public FavouritePlace getFavouritePlace(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_FAVOURITE_PLACES, new String[]{FAV_PLACE_KEY_ID, FAV_PLACE_KEY_PLACENAME, FAV_PLACE_KEY_PLACEADDRESS}, FAV_PLACE_KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        FavouritePlace favouritePlace = new FavouritePlace(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return favouritePlace;
    }

    public void deleteFavouritePlace(FavouritePlace favouritePlace) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_FAVOURITE_PLACES, FAV_PLACE_KEY_ID + "=?", new String[]{String.valueOf(favouritePlace.getId())});
        sqLiteDatabase.execSQL("UPDATE " + TABLE_FAVOURITE_PLACES + " SET id = id - 1" + " WHERE id > " + favouritePlace.getId());
        sqLiteDatabase.close();
    }

    public void deleteAllFavouritePlaces() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE * FROM " + TABLE_FAVOURITE_PLACES);
        sqLiteDatabase.close();
    }

    public ArrayList<FavouritePlace> getAllFavouritePlaces() {
        ArrayList<FavouritePlace> favouritePlacesList = new ArrayList<FavouritePlace>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_FAVOURITE_PLACES;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavouritePlace favouritePlace = new FavouritePlace(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
                // Adding favourite place to list
                favouritePlacesList.add(favouritePlace);
            } while (cursor.moveToNext());
        }
        // return voice notes list
        return favouritePlacesList;
    }

    public int getFavouritePlacesCount() {
        int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_FAVOURITE_PLACES;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();
        // return count
        return count;
    }
}