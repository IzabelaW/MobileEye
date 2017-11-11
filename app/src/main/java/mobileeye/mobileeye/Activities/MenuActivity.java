package mobileeye.mobileeye.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mobileeye.mobileeye.MenuDisplayer;
import mobileeye.mobileeye.R;

public class MenuActivity extends AppCompatActivity implements View.OnLongClickListener {

    String[] optionList = {"Error"};
    String[] selectedOptionInfoList;
    int currentOption;
    TextView optionTextView;
    MenuDisplayer menuDisplayer;

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
            selectedOptionInfoList = extras.getStringArray("selectedOptionInfoList");
        }

        optionTextView.setText(optionList[currentOption]);

        menuDisplayer = new MenuDisplayer(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuDisplayer.display(optionList[currentOption]);
            }
        }, 500);
    }


    public void optionClick(View view) {

        currentOption++;
        if(currentOption >= optionList.length) {
            currentOption = 0;
        }

        optionTextView.setText(optionList[currentOption]);
        menuDisplayer.display(optionList[currentOption]);
    }

    @Override
    public boolean onLongClick(View view) {
        menuDisplayer.display(selectedOptionInfoList[currentOption]);
        Intent data = new Intent();
        data.putExtra("selectedOption", currentOption);
        setResult(RESULT_OK, data);
        finish();

        return false;
    }
}
