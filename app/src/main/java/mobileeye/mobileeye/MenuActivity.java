package mobileeye.mobileeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements View.OnLongClickListener {

    String[] optionList = {"Error"};
    int currentOption;

    TextView optionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        optionTextView = (TextView)findViewById(R.id.optionTextView);
        optionTextView.setOnLongClickListener(this);
        currentOption = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            optionList = extras.getStringArray("optionList");
        }

        optionTextView.setText(optionList[currentOption]);
    }


    public void optionClick(View view) {
        currentOption++;
        if(currentOption >= optionList.length) {
            currentOption = 0;
        }

        optionTextView.setText(optionList[currentOption]);
    }

    @Override
    public boolean onLongClick(View view) {

        Intent data = new Intent();
        data.putExtra("selectedOption", currentOption);
        setResult(RESULT_OK, data);
        finish();

        return false;
    }
}
