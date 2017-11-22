package mobileeye.mobileeye.VoiceNotes;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;
import mobileeye.mobileeye.activity.MainActivity;
import mobileeye.mobileeye.database.DBHandler;

public class DeletingNoteActivity extends AppCompatActivity implements ReaderListener {
    private DBHandler dbHandler;
    private MenuReader menuReader;
    private Player player;
    int currentId = 0;
    int notesCount = 0;
    private String deletingCompleted = "Usuwanie notatki przebiegło pomyślnie";
    private String noNotes = "Brak notatek";
    private static final int FAKE_NOTES_NUMB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleting_note);

        TextView infoTextView = (TextView) findViewById(R.id.deletingNoteInfoTextView);;
        infoTextView.setOnTouchListener(new OnSwipeTouchListener(DeletingNoteActivity.this){

            public void onSwipeTop() {
                finish();
            }
            public void onSwipeRight() {
                listenToNextNoteTitle();

            }
            public void onSwipeLeft() {
                listenToPrevNoteTitle();
            }
            public void onSwipeBottom() {

            }

            public void onLongClick() {
                deleteNote();
            }

        });
        dbHandler = MainActivity.dbHandler;
        menuReader = new MenuReader(getApplicationContext());
        player = new Player();
        currentId = 0;

        notesCount = dbHandler.getVoiceNotesCount();
        final Handler handler = new Handler();
        if(notesCount == 0){
            notesCount = FAKE_NOTES_NUMB;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuReader.read(noNotes, DeletingNoteActivity.this);
                }
            }, 500);

        }
        else {
            listenToNextNoteTitle();
        }
    }

    private void listenToNextNoteTitle(){

        currentId++;
        if(currentId > notesCount){
            currentId = 1;
        }

        Log.i("list count", Integer.toString(dbHandler.getVoiceNotesCount()));
        Log.i("list title dir", dbHandler.getVoiceNote(currentId).getTitleDirectory());
        player.startPlaying(dbHandler.getVoiceNote(currentId).getTitleDirectory());
    }

    private void listenToPrevNoteTitle(){

        currentId--;
        if(currentId < 1){
            currentId = notesCount;
        }

        Log.i("list count", Integer.toString(dbHandler.getVoiceNotesCount()));
        Log.i("list title dir", dbHandler.getVoiceNote(currentId).getTitleDirectory());
        player.startPlaying(dbHandler.getVoiceNote(currentId).getTitleDirectory());
    }

    private void deleteNote(){
        dbHandler.deleteVoiceNote(dbHandler.getVoiceNote(currentId));
        menuReader.read(deletingCompleted, DeletingNoteActivity.this);
       notesCount = dbHandler.getVoiceNotesCount();
    }


    @Override
    public void onReadingCompleted() {
        if(notesCount == 0){
            notesCount = FAKE_NOTES_NUMB;
            menuReader.read(noNotes, DeletingNoteActivity.this);
        }
        finish();
    }
}
