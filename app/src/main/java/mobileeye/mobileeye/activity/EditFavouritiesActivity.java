package mobileeye.mobileeye.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import mobileeye.mobileeye.FavouriteNumbers.FavouriteNumber;
import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.R;

import static mobileeye.mobileeye.activity.MainActivity.dbHandler;

 enum FAVOURITE{PLACE,NUMBER};
/**
 * Created by Nexito on 05.12.2017.
 */

public class EditFavouritiesActivity extends Activity{

    private EditText nb_name;
    private EditText nb;
    private EditText place_name;
    private EditText place_address;

    private int currentNumberId=0;
    private int currentAdressId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_favourities);


        nb_name=findViewById(R.id.nb_name);
        nb=findViewById(R.id.nb);
        place_name=findViewById(R.id.place_name);
        place_address=findViewById(R.id.place_address);

        addButtonsListeners();

        showNextNumber();
        showNextAddress();



    }
    private void addButtonsListeners(){
        findViewById(R.id.add_nb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nb_name.getText().toString().equals("")&&!nb.getText().toString().equals("")) {
                    saveNumberChanges(nb_name.getText().toString(),nb.getText().toString());
                }
            }
        });

        findViewById(R.id.add_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!place_name.getText().toString().equals("")&&!place_address.getText().toString().equals("")) {
                    saveAddressChanges(place_name.getText().toString(), place_address.getText().toString());
                }
            }
        });

        findViewById(R.id.next1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showNextNumber();
            }
        });
        findViewById(R.id.next2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextAddress();
            }
        });
        findViewById(R.id.prev1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevNumber();
            }
        });
        findViewById(R.id.prev2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevAddress();
            }
        });

    }
    private void saveNumberChanges(String name, String number){
        dbHandler.deleteFavouriteNumber(dbHandler.getFavouriteNumber(currentNumberId));
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, name,number));
    }
    private void saveAddressChanges(String name, String number){
        dbHandler.deleteFavouritePlace(dbHandler.getFavouritePlace(currentNumberId));
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, name,number));


    }


    private boolean isIDInRange(FAVOURITE favourite) {
        if(favourite==FAVOURITE.NUMBER) {
            if (currentNumberId >= 1 && currentNumberId <= dbHandler.getFavouriteNumbersCount())
                return true;
        }
        else if(favourite==FAVOURITE.PLACE){
            if (currentAdressId >= 1 && currentAdressId <= dbHandler.getFavouritePlacesCount())
                return true;
        }
        else{
            throw new RuntimeException("no such favourite defined");
        }
        return false;

    }

    private void showNextAddress() {
        ++currentAdressId;
        if (isIDInRange(FAVOURITE.PLACE) ) {
        FavouritePlace favouritePlace = dbHandler.getFavouritePlace(currentAdressId);
        place_name.setText(favouritePlace.getPlaceName());
        place_address.setText(favouritePlace.getPlaceAddress());
    }
        else
            currentAdressId=dbHandler.getFavouritePlacesCount();
}


    private void showPrevAddress() {
        --currentAdressId;
        if (isIDInRange(FAVOURITE.PLACE)) {
            FavouritePlace favouritePlace = dbHandler.getFavouritePlace(currentAdressId);
            place_name.setText(favouritePlace.getPlaceName());
            place_address.setText(favouritePlace.getPlaceAddress());
        }
        else
            currentAdressId=1;
    }
    private void showNextNumber(){
        ++currentNumberId;
        if(isIDInRange(FAVOURITE.NUMBER)){
            FavouriteNumber favouriteNumber = dbHandler.getFavouriteNumber(currentNumberId);
            nb_name.setText(favouriteNumber.getContactName());
            nb.setText(favouriteNumber.getContactNumber());

        }
        else
            currentNumberId=dbHandler.getFavouriteNumbersCount()-1;

    }
    private void showPrevNumber(){
        --currentNumberId;
        if(isIDInRange(FAVOURITE.NUMBER)){
            FavouriteNumber favouriteNumber = dbHandler.getFavouriteNumber(currentNumberId);
            nb_name.setText(favouriteNumber.getContactName());
            nb.setText(favouriteNumber.getContactNumber());

        }
        else
            currentNumberId=1;
    }

}
