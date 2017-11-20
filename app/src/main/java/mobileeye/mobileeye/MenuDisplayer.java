package mobileeye.mobileeye;

import android.content.Context;
import android.content.Intent;

import mobileeye.mobileeye.activity.MenuActivity;

/**
 * Created by Kasia on 2017-11-13.
 */

public class MenuDisplayer {
    String[] optionList;

    public MenuDisplayer(String[] optionList) {

        this.optionList = optionList;
    }
    public void show(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra("optionList", optionList);
        context.startActivity(intent);
    }
}
