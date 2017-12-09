package mobileeye.mobileeye.activity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mobileeye.mobileeye.FavouriteNumber;
import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.R;

import static mobileeye.mobileeye.activity.MainActivity.dbHandler;

/**
 * Created by Nexito on 04.12.2017.
 */

public class AddFavouritiesActivity extends Activity {

    private Button add_number;
    private Button add_place;

    private EditText nb_name;
    private EditText nb;
    private EditText place_name;
    private EditText place_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_favourities);

        nb_name=findViewById(R.id.nb_name);
        nb=findViewById(R.id.nb);
        place_name=findViewById(R.id.place_name);
        place_address=findViewById(R.id.place_address);


        add_number=findViewById(R.id.add_nb);
        add_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nb_name.getText().toString().equals("")&&!nb.getText().toString().equals("")) {
                    insertFavouriteNumer(nb_name.getText().toString(), nb.getText().toString());
                    nb_name.setText("");
                    nb.setText("");
                }
            }
        });

        add_place=findViewById(R.id.add_place);
        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!place_name.getText().toString().equals("")&&!place_address.getText().toString().equals("")) {
                    insertFavouritePlace(place_name.getText().toString(), place_address.getText().toString());
                    place_name.setText("");
                    place_address.setText("");
                }
            }
        });

    }


    private void insertFavouriteNumer(String name,String number){
        dbHandler.addNewFavouriteNumber(new FavouriteNumber(dbHandler.getFavouriteNumbersCount()+1, name,number));
        Toast.makeText(this,"Dodano nowy numer", Toast.LENGTH_SHORT).show();
    }
    private void insertFavouritePlace(String name,String address){
        dbHandler.addNewFavouritePlace(new FavouritePlace(dbHandler.getFavouritePlacesCount()+1, name, address));
        Toast.makeText(this,"Dodano nowe miejsce", Toast.LENGTH_SHORT).show();
    }


}
