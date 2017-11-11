package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class FavouritePlace {

    private int id;
    private String content; // name

    public FavouritePlace(int id, String name){
        this.id = id;
        this.content = name;
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
