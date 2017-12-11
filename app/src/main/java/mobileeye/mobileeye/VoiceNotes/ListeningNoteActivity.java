package mobileeye.mobileeye.VoiceNotes;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;
import mobileeye.mobileeye.activity.Constants;
import mobileeye.mobileeye.activity.Images;
import mobileeye.mobileeye.activity.MainActivity;
import mobileeye.mobileeye.database.DBHandler;

public class ListeningNoteActivity extends AppCompatActivity implements ReaderListener{
    private DBHandler dbHandler;
    private MenuReader menuReader;
    private Player player;
    int currentId = -1;
    int notesCount = 0;
    private String noNotes = "Brak notatek";
    private ArrayList<VoiceNote> voiceNotesList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_note);

        TextView infoTextView = (TextView) findViewById(R.id.listeningNoteInfoTextView);


        infoTextView.setOnTouchListener(new OnSwipeTouchListener(ListeningNoteActivity.this){

            public void onSwipeTop() {
                Images.setCurrentMenu(Constants.VOICE_NOTES);
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
        currentId = -1;


        voiceNotesList = dbHandler.getAllVoiceNotes();
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
        if(currentId >= voiceNotesList.size()){
            currentId = 0;
        }

        Log.i("list count next", Integer.toString(dbHandler.getVoiceNotesCount()));
        Log.i("list id ", Integer.toString(currentId));
        player.startPlaying(dbHandler.getVoiceNote(voiceNotesList.get(currentId).getDate()).getTitleDirectory());
    }


    private void listenToPrevNoteTitle(){

        currentId--;
        if(currentId < 0){
            currentId = voiceNotesList.size() - 1;
        }

        Log.i("list count", Integer.toString(dbHandler.getVoiceNotesCount()));

        player.startPlaying(dbHandler.getVoiceNote(voiceNotesList.get(currentId).getDate()).getTitleDirectory());
    }

    private void listenToNote(){
        player.startPlaying(dbHandler.getVoiceNote(voiceNotesList.get(currentId).getDate()).getContentDirectory());
        Log.i("list note dir", dbHandler.getVoiceNote(voiceNotesList.get(currentId).getDate()).getContentDirectory());
    }


    @Override
    public void onReadingCompleted() {
       // Images.setCurrentMenu(Constants.VOICE_NOTES);
        finish();
    }

    @Override
    public void onBackPressed() {
        Images.setCurrentMenu(Constants.VOICE_NOTES);
        super.onBackPressed();
    }
}

