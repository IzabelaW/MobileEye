package mobileeye.mobileeye;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Tomasz on 02.11.2017.
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
