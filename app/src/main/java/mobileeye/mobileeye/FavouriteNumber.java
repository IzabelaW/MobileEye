package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class FavouriteNumber {

    private int id;
    private String content; // phone number

    public FavouriteNumber(int id, String content){
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
