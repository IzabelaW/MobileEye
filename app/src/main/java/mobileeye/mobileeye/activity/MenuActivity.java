package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;

public class MenuActivity extends AppCompatActivity implements ReaderListener {

    String[] optionList = {"Error"};
    String[] selectedOptionInfoList;
    int currentOption;
    TextView optionTextView;
    MenuReader menuReader;
    private static final int CANCEL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        optionTextView = (TextView)findViewById(R.id.optionTextView);
        optionTextView.setOnTouchListener(new OnSwipeTouchListener(MenuActivity.this){

            public void onSwipeTop() {
                Intent data = new Intent();
                data.putExtra("selectedOption", CANCEL);
                setResult(RESULT_OK, data);
                finish();
            }
            public void onSwipeRight() {
                nextOptionClick(optionTextView);
            }
            public void onSwipeLeft() {
                prevOptionClick(optionTextView);
            }
            public void onSwipeBottom() {

            }

            public void onLongClick() {
                menuReader.read(selectedOptionInfoList[currentOption], MenuActivity.this);
            }

        });
        currentOption = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            optionList = extras.getStringArray("optionList");
            selectedOptionInfoList = extras.getStringArray("selectedOptionInfoList");
        }

        optionTextView.setText(optionList[currentOption]);

        menuReader = new MenuReader(getApplicationContext());
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuReader.read(optionList[currentOption]);
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();

       currentOption = 0;
    }


    public void nextOptionClick(View view) {

        currentOption++;
        if(currentOption >= optionList.length) {
            currentOption = 0;
        }

        optionTextView.setText(optionList[currentOption]);
        menuReader.read(optionList[currentOption]);
    }

    public void prevOptionClick(View view) {

        currentOption--;
        if(currentOption < 0) {
            currentOption = optionList.length - 1;
        }

        optionTextView.setText(optionList[currentOption]);
        menuReader.read(optionList[currentOption]);
    }


    @Override
    public void onReadingCompleted() {
        Intent data = new Intent();
        data.putExtra("selectedOption", currentOption);
        setResult(RESULT_OK, data);
        finish();

    }


}
