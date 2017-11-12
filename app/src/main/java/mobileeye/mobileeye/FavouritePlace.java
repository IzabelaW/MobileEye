package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class FavouritePlace {

    private int id;
    private String placeName;
    private String placeAddress;

    public FavouritePlace(int id, String placeName, String placeAddress){
        this.id = id;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
    }

    public int getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() { return placeAddress; }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceAddress(String placeAddress) { this.placeAddress = placeAddress; }
}
