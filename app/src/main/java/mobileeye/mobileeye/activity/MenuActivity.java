package mobileeye.mobileeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;

public class MenuActivity extends AppCompatActivity implements ReaderListener{

    private static final int NAVIGATION = 1;
    private static final int FAVOURITE_NUMBERS = 2;
    private static final int CANCEL = 3;

    private int activity;

    private String[] optionList = { "Error" };
    private ArrayList<String> optionArrayList;
    private String[] selectedOptionInfoList;
    private ArrayList<String> selectedOptionInfoArrayList;

    private int currentOption;
    private TextView optionTextView;
    private MenuReader menuReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();
        activity = extras.getInt("activity");

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
                switch (activity) {
                    case NAVIGATION:
                    case FAVOURITE_NUMBERS:
                        menuReader.read(selectedOptionInfoArrayList.get(currentOption), MenuActivity.this);
                        break;
                    default:
                        menuReader.read(selectedOptionInfoList[currentOption], MenuActivity.this);
                        break;
                }
            }

        });
        currentOption = 0;

        if (extras != null) {
            switch (activity) {
                case NAVIGATION:
                case FAVOURITE_NUMBERS:
                    optionArrayList = extras.getStringArrayList("optionList");
                    selectedOptionInfoArrayList = extras.getStringArrayList("selectedOptionInfoList");
                    optionTextView.setText(optionArrayList.get(currentOption));
                    menuReader = new MenuReader(getApplicationContext());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuReader.read(optionArrayList.get(currentOption));
                        }
                    }, 500);
                    break;
                default:
                    optionList = extras.getStringArray("optionList");
                    selectedOptionInfoList = extras.getStringArray("selectedOptionInfoList");
                    optionTextView.setText(optionList[currentOption]);
                    menuReader = new MenuReader(getApplicationContext());
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            menuReader.read(optionList[currentOption]);
                        }
                    }, 500);
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

       currentOption = 0;
    }


    public void nextOptionClick(View view) {

        currentOption++;

        switch (activity){
            case NAVIGATION:
            case FAVOURITE_NUMBERS:
                if(currentOption >= optionArrayList.size()) {
                    currentOption = 0;
                }

                optionTextView.setText(optionArrayList.get(currentOption));
                menuReader.read(optionArrayList.get(currentOption));
                break;
            default:
                if(currentOption >= optionList.length) {
                    currentOption = 0;
                }

                optionTextView.setText(optionList[currentOption]);
                menuReader.read(optionList[currentOption]);
                break;
        }
    }

    public void prevOptionClick(View view) {

        currentOption--;

        switch (activity) {
            case NAVIGATION:
            case FAVOURITE_NUMBERS:
                if(currentOption < 0) {
                    currentOption = optionArrayList.size() - 1;
                }
                optionTextView.setText(optionArrayList.get(currentOption));
                menuReader.read(optionArrayList.get(currentOption));                break;
            default:
                if(currentOption < 0) {
                    currentOption = optionList.length - 1;
                }
                optionTextView.setText(optionList[currentOption]);
                menuReader.read(optionList[currentOption]);                break;
        }
    }


    @Override
    public void onReadingCompleted() {
        Intent data = new Intent();
        data.putExtra("selectedOption", currentOption);
        setResult(RESULT_OK, data);
        finish();

    }



}
