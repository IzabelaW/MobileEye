package mobileeye.mobileeye.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mobileeye.mobileeye.R;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

//        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
//        optionMenuIntent.putExtra("optionList", optionList);
//        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
//        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }

    public void onClick(View view){

    }
}
