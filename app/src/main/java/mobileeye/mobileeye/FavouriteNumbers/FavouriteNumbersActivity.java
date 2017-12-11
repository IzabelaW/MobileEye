package mobileeye.mobileeye.FavouriteNumbers;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.OnSwipeTouchListener;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;
import mobileeye.mobileeye.activity.Constants;
import mobileeye.mobileeye.activity.Images;
import mobileeye.mobileeye.activity.MainActivity;
import mobileeye.mobileeye.database.DBHandler;

public class FavouriteNumbersActivity extends AppCompatActivity implements ReaderListener {
    private DBHandler dbHandler;
    private MenuReader menuReader;
    private ArrayList<FavouriteNumber> favouriteNumbersList = new ArrayList<>();
    private int currentId;
    TextView infoTextView;
    private static final int REQUEST_CALL_PHONE = 1;
    private String noNumbers = "Brak ulubionych numerów";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_numbers);

        infoTextView = (TextView) findViewById(R.id.favouriteNumbersInfoTextView);


        infoTextView.setOnTouchListener(new OnSwipeTouchListener(FavouriteNumbersActivity.this) {

            public void onSwipeTop() {
                Images.setCurrentMenu(Constants.MAIN_MENU);
                finish();
            }

            public void onSwipeRight() {
                listenToNextContactName();
            }

            public void onSwipeLeft() {
                listenToPrevContactName();
            }

            public void onSwipeBottom() {

            }

            public void onLongClick() {
                call();
            }

        });

        dbHandler = MainActivity.dbHandler;
        favouriteNumbersList = dbHandler.getAllFavouriteNumbers();
        menuReader = new MenuReader(getApplicationContext());
        currentId = -1;
        final Handler handler = new Handler();
        Log.i("favourite ", Integer.toString(favouriteNumbersList.size()));
        if (favouriteNumbersList.size() == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuReader.read(noNumbers, FavouriteNumbersActivity.this);
                }
            }, 500);

        }
        else handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listenToNextContactName();
            }
        }, 500);

        Images.setNextImg( 1, infoTextView, this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CALL_PHONE) {
            Log.i("call ", "Received response for contact permissions request.");

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void listenToNextContactName(){
        currentId++;
        if(currentId >= favouriteNumbersList.size()){
            currentId = 0;
        }
        infoTextView.setText(favouriteNumbersList.get(currentId).getContactName());
        menuReader.read(favouriteNumbersList.get(currentId).getContactName());

    }

    private void listenToPrevContactName(){
        currentId--;
        if(currentId < 0){
            currentId = favouriteNumbersList.size() - 1;
        }
        infoTextView.setText(favouriteNumbersList.get(currentId).getContactName());
        menuReader.read(favouriteNumbersList.get(currentId).getContactName());
    }

    private void call(){
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + favouriteNumbersList.get(currentId).getContactNumber()));

        int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        }
        startActivity(callIntent);
    }


    @Override
    public void onReadingCompleted() {
        finish();
    }
    @Override
    public void onBackPressed() {
        Images.setCurrentMenu(Constants.MAIN_MENU);
        super.onBackPressed();

    }
}
