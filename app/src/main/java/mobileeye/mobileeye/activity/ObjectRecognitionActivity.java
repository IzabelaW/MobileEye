package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mobileeye.mobileeye.R;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class ObjectRecognitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_recognition);
        // hello
    }

    @Override
    public void onBackPressed() {
        Images.setCurrentMenu(Constants.MAIN_MENU);
        super.onBackPressed();
    }
}
