package mobileeye.mobileeye;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class VoiceNote {

    private int id;
    private String titleDirectory;
    private String contentDirectory;

    public VoiceNote(int id, String title, String directory){
        this.id = id;
        this.titleDirectory = title;
        this.contentDirectory = directory;
    }

    public int getId() {
        return id;
    }

    public String getTitleDirectory() {
        return titleDirectory;
    }

    public String getContentDirectory() {
        return contentDirectory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitleDirectory(String titleDirectory) {
        this.titleDirectory = titleDirectory;
    }

    public void setContentDirectory(String contentDirectory) {
        this.contentDirectory = contentDirectory;
    }


}
