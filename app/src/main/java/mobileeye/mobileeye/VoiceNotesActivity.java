package mobileeye.mobileeye;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class VoiceNotesActivity extends AppCompatActivity {

    private static final int OPTION_MENU_RESULT = 1;

    private static final int ADD_VOICENOTE = 0;
    private static final int DELETE_VOICENOTE = 1;
    private static final int LISTEN_VOICENOTE = 2;

    private String[] optionList = {"Dodaj nową notatkę", "Usuń istniejącą notatkę", "Przeglądaj swoje notatki"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);
                Toast.makeText(this, "Selected " + selectedOption, Toast.LENGTH_LONG).show();
            }

            switch (selectedOption) {
                case ADD_VOICENOTE:
                    Intent voiceNotesIntent = new Intent(this, VoiceNotesActivity.class);
                    startActivity(voiceNotesIntent);
                case DELETE_VOICENOTE:
                    ;
                case LISTEN_VOICENOTE:
                    ;
            }
        }
    }

}
