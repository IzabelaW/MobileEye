package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class FavouriteNumber {

    private int id;
    private String contactNumber; // phone number
    private String contactName;

    public FavouriteNumber(int id, String contactName, String contactNumber){
        this.id = id;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactName() { return contactName; }

    public void setId(int id) {
        this.id = id;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setContactName(String contactName) { this.contactName =contactName; }
}
