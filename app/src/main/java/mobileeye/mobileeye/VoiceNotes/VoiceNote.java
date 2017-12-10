package mobileeye.mobileeye.VoiceNotes;

/**
 * Created by izabelawojciak on 11.11.2017.
 */

public class VoiceNote {


    private String date;
    private String titleDir;
    private String noteBodyDir;

    public VoiceNote(String date, String titleDir, String noteBodyDir){
        this.date = date;
        this.titleDir = titleDir;
        this.noteBodyDir = noteBodyDir;
    }

    public String getDate() {
        return date;
    }

    public String getTitleDirectory() {
        return titleDir;
    }

    public String getContentDirectory() {
        return noteBodyDir;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitleDir(String titleDir) {
        this.titleDir = titleDir;
    }

    public void setNoteBodyDir(String noteBodyDir) {
        this.noteBodyDir = noteBodyDir;
    }


}
