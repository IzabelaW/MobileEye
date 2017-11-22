package mobileeye.mobileeye.VoiceNotes;

import android.annotation.SuppressLint;
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

public class ListeningNoteActivity extends AppCompatActivity implements ReaderListener{
    private DBHandler dbHandler;
    private MenuReader menuReader;
    private Player player;
    int currentId = 0;
    int notesCount = 0;
    private String noNotes = "Brak notatek";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_note);

        TextView infoTextView = (TextView) findViewById(R.id.listeningNoteInfoTextView);


        infoTextView.setOnTouchListener(new OnSwipeTouchListener(ListeningNoteActivity.this){

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
                listenToNote();
            }

        });

        dbHandler = MainActivity.dbHandler;
        menuReader = new MenuReader(getApplicationContext());
        player = new Player();
        currentId = 0;

        notesCount = dbHandler.getVoiceNotesCount();
        final Handler handler = new Handler();
        if(notesCount == 0){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuReader.read(noNotes, ListeningNoteActivity.this);
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

        Log.i("list count next", Integer.toString(dbHandler.getVoiceNotesCount()));
        Log.i("list id ", Integer.toString(currentId));
        Log.i("list title dir next", dbHandler.getVoiceNote(currentId).getTitleDirectory());
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

    private void listenToNote(){
        player.startPlaying(dbHandler.getVoiceNote(currentId).getContentDirectory());
        Log.i("list note dir", dbHandler.getVoiceNote(currentId).getContentDirectory());
    }


    @Override
    public void onReadingCompleted() {
        finish();
    }
}
