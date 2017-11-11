package mobileeye.mobileeye;

import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version 
    private static final int DATABASE_VERSION = 1; 
    // Database Name 
    private static final String DATABASE_NAME = "MobileEyeDB"; 
    // Voice notes table name 
    private static final String TABLE_VOICE_NOTES = "VoiceNotes"; 
    // Voice notes table columns names 
    private static final String KEY_ID = "id"; 
    private static final String KEY_TITLE = "title"; 
    private static final String KEY_DIRECTORY = "directory";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) { 
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { 
        String CREATE_VOICENOTES_TABLE = "CREATE TABLE " + TABLE_VOICE_NOTES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_DIRECTORY + " TEXT " + ")"; 
        sqLiteDatabase.execSQL(CREATE_VOICENOTES_TABLE); 
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { 
        // Drop older table if existed 
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VOICE_NOTES); 
        // Creating tables again 
        onCreate(sqLiteDatabase); 
    }

    public void addNewVoiceNote(VoiceNote voiceNote){ 
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); 
        ContentValues values = new ContentValues(); 
        values.put(KEY_TITLE, voiceNote.getTitle()); // Voice note title 
        values.put(KEY_DIRECTORY, voiceNote.getDirectory()); // Voice note content 
        // Inserting Row 
        sqLiteDatabase.insert(TABLE_VOICE_NOTES, null, values); 
        sqLiteDatabase.close(); // Closing database connection 
    }

    public VoiceNote getVoiceNote(int id){ 
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase(); 
        Cursor cursor = sqLiteDatabase.query(TABLE_VOICE_NOTES, new String[] { KEY_ID, KEY_TITLE, KEY_DIRECTORY }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null); 
        if (cursor != null) 
            cursor.moveToFirst(); 
        VoiceNote voiceNote = new VoiceNote(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2)); // return shop 
        return voiceNote; 
    } 

    public int getVoiceNotesCount(){
        String countQuery = "SELECT * FROM " + TABLE_VOICE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public void deleteVoiceNote(VoiceNote voiceNote){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOICE_NOTES, KEY_ID + " = ?", new String[] { String.valueOf(voiceNote.getId()) });
        db.close();
    }

}