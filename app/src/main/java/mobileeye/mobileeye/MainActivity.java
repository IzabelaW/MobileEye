package mobileeye.mobileeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int OPTION_MENU_RESULT = 1;

    String[] optionList = {"Option1", "Option2", "Option3"};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Selected " + data.getIntExtra("selectedOption", 0), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("optionList", optionList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }
}
