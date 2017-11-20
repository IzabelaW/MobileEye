package mobileeye.mobileeye.VoiceNotes;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class VoiceNote {

    private int id;
    private String titleDir;
    private String noteBodyDir;

    public VoiceNote(int id, String titleDir, String noteBodyDir){
        this.id = id;
        this.titleDir = titleDir;
        this.noteBodyDir = noteBodyDir;
    }

    public int getId() {
        return id;
    }

    public String getTitleDirectory() {
        return titleDir;
    }

    public String getContentDirectory() {
        return noteBodyDir;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitleDir(String titleDir) {
        this.titleDir = titleDir;
    }

    public void setNoteBodyDir(String noteBodyDir) {
        this.noteBodyDir = noteBodyDir;
    }


}
