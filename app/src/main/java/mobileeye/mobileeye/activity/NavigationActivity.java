package mobileeye.mobileeye.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.MyService;
import mobileeye.mobileeye.R;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class NavigationActivity extends AppCompatActivity{

    private static final int OPTION_MENU_RESULT = 1;
    private ArrayList<String> optionList;
    private ArrayList<String> selectedOptionInfoList;

    private FavouritePlace selectedPlace;
    private Location currentLocation;

    private LocationReceiver locationReceiver;

    public class LocationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            currentLocation = intent.getExtras().getParcelable("NEW_LOCATION");
//            Toast.makeText(context, "ODEBRANO: " + currentLocation.toString(),Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        locationReceiver = new LocationReceiver();

        optionList = createOptionList();
        selectedOptionInfoList = createSelectedOptionInfoList();

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("activity", 1);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);
                selectedPlace = MainActivity.dbHandler.getFavouritePlace(selectedOption + 1);
//                Toast.makeText(this, "Selected: " + selectedPlace.getPlaceName(), Toast.LENGTH_LONG).show();
                downloadRoute();

            }
        }
    }

    private void downloadRoute(){
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        String destination = selectedPlace.getPlaceAddress();

        String url = getDirectionsUrl(origin,destination);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, String destination) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + destination.replaceAll(" ","");

        // Sensor enabled
        String mode = "mode=walking";

        //Langouage of output
        String language = "language=pl";

        //API key
        String APIkey = String.valueOf(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + language + "&" + APIkey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private ArrayList<String> createOptionList(){
        ArrayList<String> optionList = new ArrayList<>();

        for (FavouritePlace favouritePlace : MainActivity.dbHandler.getAllFavouritePlaces()){
            optionList.add(favouritePlace.getPlaceName());
        }
        return optionList;
    }

    private ArrayList<String> createSelectedOptionInfoList(){
        ArrayList<String> selectedOptionInfoList = new ArrayList<>();
        String selectedOption = "";

        for (FavouritePlace favouritePlace : MainActivity.dbHandler.getAllFavouritePlaces()){
            selectedOption = "Wybrano: " + favouritePlace.getPlaceName();
            selectedOptionInfoList.add(selectedOption);
        }
        return selectedOptionInfoList;
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(locationReceiver,
                new IntentFilter("LOCATION_CHANGED"));
//        Toast.makeText(this,"REGISTERED",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationReceiver);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("JSON",data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //parsowanie outputu w formacie JSON

        }
    }
}

