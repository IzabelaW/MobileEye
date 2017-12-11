package mobileeye.mobileeye.activity;

import android.content.Context;
import android.view.View;

import mobileeye.mobileeye.R;

/**
 * Created by Nexito on 03.12.2017.
 */

public class Images {

    private static int[] drawablesMainMenu = {R.drawable.notatki_glosowe, R.drawable.czytanie, R.drawable.nawigacja,R.drawable.ustawienia,R.drawable.numery,R.drawable.guide};
    private static int[] drawablesVoiceNotes = {R.drawable.dodaj, R.drawable.usun, R.drawable.przegladaj};

    private static int current_menu=Constants.MAIN_MENU;

    public static void setNextImg( int option, View textView, Context context) {


        if (option >= 0) {
            switch (current_menu) {
                case Constants.MAIN_MENU: textView.setBackgroundDrawable(context.getResources().getDrawable(drawablesMainMenu[option]));
                                            break;
                case Constants.VOICE_NOTES: textView.setBackgroundDrawable(context.getResources().getDrawable(drawablesVoiceNotes[option]));
                    break;
                case Constants.NAVIGATION:  textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.nawigacja));

                    break;
                case Constants.OBJECT_RECOGNITION: break;
                case Constants.FAVOURITE_NUMBERS:
                    textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.numery));
                    break;
                case Constants.GUIDE:
                        break;
            }
        }


    }
    public static void setCurrentMenu(int menu){
            current_menu=menu;
           // setNextImg(0,textView,context);
        }


}
