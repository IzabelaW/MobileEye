package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class VoiceNote {

    private int id;
    private String title;
    private String directory;

    public VoiceNote(int id, String title, String directory){
        this.id = id;
        this.title = title;
        this.directory = directory;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirectory() {
        return directory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }


}
