package mobileeye.mobileeye.Navigation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.MenuReader;
import mobileeye.mobileeye.R;
import mobileeye.mobileeye.ReaderListener;
import mobileeye.mobileeye.activity.Constants;
import mobileeye.mobileeye.activity.Images;
import mobileeye.mobileeye.activity.MainActivity;
import mobileeye.mobileeye.activity.MenuActivity;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class NavigationActivity extends AppCompatActivity implements ReaderListener, OnMapReadyCallback {

    private static final int OPTION_MENU_RESULT = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CANCEL = -1;

    private LocationReceiver locationReceiver;
    private MenuReader menuReader;
    public static GoogleMap googleMap;

    private FavouritePlace selectedPlace;
    private Location currentLocation;
    private int currentStep = 0;
    private List<HashMap<String, String>> steps;

    private boolean downloadPath = false;
    private boolean readingDone = false;
    private boolean locationPermission = false;
    private boolean messageSend = false;
    private boolean currentLocationNotNull = false;

    private ArrayList<String> optionList;
    private ArrayList<String> selectedOptionInfoList;
    private ArrayList<Marker> markers;

    public class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            currentLocation = intent.getExtras().getParcelable("NEW_LOCATION");

            if (currentLocation != null)
                currentLocationNotNull = true;

            if (currentStep >= 1 && readingDone) {
                checkForUpdates();
            }
        }
    }


    private void checkForUpdates() {

        if (currentStep >= steps.size()) {
            readingDone = false;
            menuReader.read("Jesteś na miejscu. Nawigacja zakończona.", NavigationActivity.this);
            finish();
        }
        else {
            double stepLat = Double.parseDouble(steps.get(currentStep).get("startLat"));
            double stepLng = Double.parseDouble(steps.get(currentStep).get("startLng"));

            Location stepLocation = new Location("");
            stepLocation.setLatitude(stepLat);
            stepLocation.setLongitude(stepLng);

            if (currentStep + 1 < steps.size()) {
                Location nextStepLocation = new Location("");

                double nextStepLat = Double.parseDouble(steps.get(currentStep + 1).get("startLat"));
                double nextStepLng = Double.parseDouble(steps.get(currentStep + 1).get("startLng"));

                nextStepLocation.setLatitude(nextStepLat);
                nextStepLocation.setLongitude(nextStepLng);

                if (currentLocation.distanceTo(stepLocation) <= 10) {
                    readingDone = false;
                    menuReader.read(steps.get(currentStep).get("htmlInstruction") + ". Za " + steps.get(currentStep).get("distance") + "metrów" + steps.get(currentStep + 1).get("htmlInstruction"), NavigationActivity.this);
                }
                else if (currentLocation.distanceTo(stepLocation) > currentLocation.distanceTo(nextStepLocation)) {
                    currentStep++;
                }
            }
            else if (currentLocation.distanceTo(stepLocation) <= 10) {
                readingDone = false;
                menuReader.read(steps.get(currentStep).get("htmlInstruction"), NavigationActivity.this);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        menuReader = new MenuReader(getApplicationContext());
        menuReader.setReaderListener(this);

        OptionListCreator optionListCreator = new OptionListCreator();

        optionList = optionListCreator.createOptionList();
        selectedOptionInfoList = optionListCreator.createSelectedOptionInfoList();
        markers = new ArrayList<>();
        locationReceiver = new LocationReceiver();

        Intent optionMenuIntent = new Intent(this, MenuActivity.class);
        optionMenuIntent.putExtra("activity", 1);
        optionMenuIntent.putExtra("optionList", optionList);
        optionMenuIntent.putExtra("selectedOptionInfoList", selectedOptionInfoList);
        startActivityForResult(optionMenuIntent, OPTION_MENU_RESULT);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int selectedOption = 0;

        if (requestCode == OPTION_MENU_RESULT && data != null) {
            if (resultCode == RESULT_OK) {
                selectedOption = data.getIntExtra("selectedOption", 0);

                if (selectedOption == -1) {
                    Intent d = new Intent();
                    d.putExtra("selectedOption", CANCEL);
                    setResult(RESULT_OK, data);
                    finish();
//                    startActivity(MainActivity.starterIntent);
                } else {

                    SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
                    mapFragment.getMapAsync(this);

                    selectedPlace = MainActivity.dbHandler.getFavouritePlace(selectedOption + 1);

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
        }
    }

    private void downloadRoute() {
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        String destination = selectedPlace.getPlaceAddress();

        String url = getDirectionsUrl(origin, destination);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, String destination) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + destination.replaceAll(" ", "");

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        //Langouage of output
        String language = "language=pl";

        //API key
        String APIkey = String.valueOf(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + language + "&" + APIkey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.d("JSON", data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
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
    }


    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size() - 1; i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            googleMap.addPolyline(lineOptions);

            steps = result.get(1);

            double endLat = Double.parseDouble(steps.get(steps.size()-1).get("endLat"));
            double endLng = Double.parseDouble(steps.get(steps.size()-1).get("endLng"));
            LatLng endLatLng = new LatLng(endLat,endLng);

            MarkerOptions options = new MarkerOptions();
            options.position(endLatLng);
            Marker marker = googleMap.addMarker(options);
            markers.add(marker);
            centerCamera();

//            readingDone = false;
            menuReader.read(steps.get(currentStep).get("htmlInstruction") + ". Za " + steps.get(currentStep).get("distance") + "metrów" + steps.get(currentStep + 1).get("htmlInstruction"), NavigationActivity.this);
        }
    }

    private void centerCamera() throws NullPointerException {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();
        int padding = 80;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate, 1500, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(locationReceiver,
                new IntentFilter("LOCATION_CHANGED"));

        Log.d("RECEIVER","REGISTERED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Images.setCurrentMenu(Constants.MAIN_MENU);
        unregisterReceiver(locationReceiver);
        Log.d("RECEIVER","UNREGISTERED");

    }

    @Override
    public void onReadingCompleted() {
        if (downloadPath) {
            downloadPath = false;
        } else {
            currentStep++;
            if (!messageSend)
                sendMessage();
        }
        readingDone = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                locationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (locationPermission) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);

            AsyncTask asyncTask = new AsyncTask() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    downloadPath = true;
                    menuReader.read("Proszę czekać. Pobieram trasę", NavigationActivity.this);
                }

                @Override
                protected Object doInBackground(Object[] objects) {
                    while (true){
                        if (currentLocationNotNull){
                            break;
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object object) {
                    super.onPostExecute(object);
                    MarkerOptions options = new MarkerOptions();
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    options.position(latLng);
                    Marker marker = googleMap.addMarker(options);
                    markers.add(marker);
                    downloadRoute();
                }

            };

            asyncTask.execute();

        } else {
            finish();
        }
    }

    private void sendMessage() {
        Intent intent = new Intent("LOCATION_UPDATES_REQUEST");
        // Adding some data
        intent.putExtra("LOCATION_UPDATES_REQUEST", true);
        getApplicationContext().sendBroadcast(intent);
        messageSend = true;
    }
}
