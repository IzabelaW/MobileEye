package mobileeye.mobileeye.Navigation;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by izabelawojciak on 27.11.2017.
 */

public class DirectionsJSONParser {

    private static final String HTML_INSTRUCTION = "htmlInstruction";
    private static final String DISTANCE = "distance";
    private static final String START_LAT = "startLat";
    private static final String START_LNG = "startLng";
    private static final String END_LAT = "endLat";
    private static final String END_LNG = "endLng";


    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray routesJSONArray = null;
        JSONArray legsJSONArray = null;
        JSONArray stepsJSONArray = null;

        String htmlInstruction = "";
        String distance = "";
        String startLat = "";
        String startLng = "";
        String endLat = "";
        String endLng = "";

        try {

            routesJSONArray = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i = 0; i < routesJSONArray.length(); i++){

                legsJSONArray = ((JSONObject)routesJSONArray.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();
                List instructions = new ArrayList<HashMap<String,String>>();

                /** Traversing all legs */
                for(int j = 0; j < legsJSONArray.length(); j++){

                    stepsJSONArray = ((JSONObject)legsJSONArray.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k = 0; k < stepsJSONArray.length(); k++){

                        JSONObject step = stepsJSONArray.getJSONObject(k);
                        String polyline = "";
                        polyline = step.getJSONObject("polyline").getString("points");
                        List list = decodePoly(polyline);

                        /** Traversing all points */
                            for(int l=0;l <list.size();l++){

                                HashMap<String,String> singleStep = new HashMap();

                                singleStep.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                singleStep.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );

                                path.add(singleStep);
                            }

                        HashMap<String,String> singleStepinstructions = new HashMap();

                        htmlInstruction = clearHTMLInstructions(step.getString("html_instructions"));
                        distance = step.getJSONObject("distance").getString("value");
                        startLat = step.getJSONObject("start_location").getString("lat");
                        startLng = step.getJSONObject("start_location").getString("lng");
                        endLat = step.getJSONObject("end_location").getString("lat");
                        endLng = step.getJSONObject("end_location").getString("lng");

                        singleStepinstructions.put(HTML_INSTRUCTION, htmlInstruction);
                        singleStepinstructions.put(DISTANCE, distance);
                        singleStepinstructions.put(START_LAT, startLat);
                        singleStepinstructions.put(START_LNG, startLng);
                        singleStepinstructions.put(END_LAT, endLat);
                        singleStepinstructions.put(END_LNG, endLng);

                        instructions.add(singleStepinstructions);
                    }
                    routes.add(path);
                    routes.add(instructions);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }

    private String clearHTMLInstructions(String htmlInstruction){
        return Jsoup.parse(htmlInstruction).text();
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
