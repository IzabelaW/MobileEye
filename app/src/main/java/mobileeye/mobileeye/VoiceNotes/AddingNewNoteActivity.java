package mobileeye.mobileeye.VoiceNotes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;
import mobileeye.mobileeye.activity.MainActivity;
import mobileeye.mobileeye.database.DBHandler;

public class AddingNewNoteActivity extends AppCompatActivity implements  ReaderListener {

    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;

    private DBHandler dbHandler;
    private MenuReader menuReader;
    private Recorder recorder;
    private String enterTitle = "Podaj tytuł notatki";
    private String enterNoteBody = "Podaj treść notatki";
    private String recordingCompleted = "Nagrywanie notatki zakończone";
    private boolean titleRecordingInProgress = false;
    private boolean noteBodyRecordingInProgress = false;
    private String newTitleFile;
    private String newNoteFile;
    private boolean waitingForRecording = false;
    private String fileToRecordTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_new_note);

        TextView infoTextView = (TextView) findViewById(R.id.addingNewNoteInfoTextView);
        infoTextView.setOnTouchListener(new OnSwipeTouchListener(AddingNewNoteActivity.this){

            public void onSwipeTop() {
                finish();
            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {

            }
            public void onSwipeBottom() {

            }

            public void onClick() {
                recorder.stopRecording();

                if(noteBodyRecordingInProgress) {
                    saveRecordedNote();
                    stopNoteRecording();
                }
                if(titleRecordingInProgress){
                    //menuReader.read(enterNoteBody);
                    startVoiceNoteRecording(NotePart.NOTE_BODY);
                    titleRecordingInProgress = false;
                    noteBodyRecordingInProgress = true;
                }
            }

        });
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        menuReader = new MenuReader(getApplicationContext());
        menuReader.setReaderListener(this);
        recorder = new Recorder();
        dbHandler = MainActivity.dbHandler;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addVoiceNote();
            }
        }, 500);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            finish();
        }

    }

    private void addVoiceNote() {
        newTitleFile = prepareFileToSaveNewRecording(NotePart.TITLE);
        Toast.makeText(this, newTitleFile, Toast.LENGTH_LONG).show();
        newNoteFile = prepareFileToSaveNewRecording(NotePart.NOTE_BODY);
        startVoiceNoteRecording(NotePart.TITLE);
        titleRecordingInProgress = true;
    }



    private void startVoiceNoteRecording(NotePart notePart){
        switch(notePart){
            case TITLE:
                menuReader.read(enterTitle, this);
                fileToRecordTo = newTitleFile;
                waitingForRecording = true;
                //recorder.startRecording(newTitleFile);
                break;
            case NOTE_BODY:
                menuReader.read(enterNoteBody, this);
                fileToRecordTo = newNoteFile;
                waitingForRecording = true;
                //recorder.startRecording(newNoteFile);
                break;
        }
    }

    private void saveRecordedNote(){
        VoiceNote newVoiceNote = new VoiceNote(dbHandler.getVoiceNotesCount() + 1, newTitleFile, newNoteFile);
        dbHandler.addNewVoiceNote(newVoiceNote);
    }

    private void stopNoteRecording(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuReader.read(recordingCompleted);
            }
        }, 100);

        finish();
    }

    private String prepareFileToSaveNewRecording(NotePart notePart){
        String newFileName;
        String newNoteStringId = Integer.toString(dbHandler.getVoiceNotesCount() + 1);

       // String newNoteStringId = Integer.toString(1);
      newFileName = getCacheDir().getAbsolutePath();
        switch(notePart){
            case TITLE:
                newFileName += "/title" + newNoteStringId + ".3gp";
                break;
            case NOTE_BODY:
                newFileName += "/rec" + newNoteStringId + ".3gp";
                break;
            default:
                newFileName += "/rec" + newNoteStringId + ".3gp";
                break;
        }
        Log.i("list adding new file ", newFileName);
        return newFileName;
    }


    @Override
    public void onReadingCompleted() {
        Log.i("compl ", "completed");
        if(waitingForRecording){
            recorder.startRecording(fileToRecordTo);
            waitingForRecording = false;
        }
    }

    public enum NotePart{
        TITLE, NOTE_BODY;
    }
}
