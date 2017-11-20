package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.MenuDisplayer;
import mobileeye.mobileeye.VoiceNote;
import mobileeye.mobileeye.database.DBHandler;
import mobileeye.mobileeye.FavouriteNumber;
import mobileeye.mobileeye.R;

public class MainActivity extends AppCompatActivity{

    private int currentOption;
    private TextView optionTextView;
    private MenuDisplayer menuDisplayer;

    public static OptionsClass optionsLogic;

    private MainMenuClass mainMenuClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ///database
        new DataBaseClass(this).initializeDataBase();

        mainMenuClass=new MainMenuClass();

        ///text view
        optionTextView = (TextView) findViewById(R.id.optionTextView);

        ///menu displayer initialization
        menuDisplayer = new MenuDisplayer(this);

        //options menager class initialization
        optionsLogic=new OptionsClass(menuDisplayer,optionTextView);
        mainMenuClass.initialize();
    }


}
