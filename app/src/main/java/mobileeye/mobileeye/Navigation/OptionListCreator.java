package mobileeye.mobileeye.Navigation;

import java.util.ArrayList;

import mobileeye.mobileeye.FavouritePlace;
import mobileeye.mobileeye.activity.MainActivity;

/**
 * Created by izabelawojciak on 02.12.2017.
 */

public class OptionListCreator {
    public ArrayList<String> createOptionList() {
        ArrayList<String> optionList = new ArrayList<>();

        for (FavouritePlace favouritePlace : MainActivity.dbHandler.getAllFavouritePlaces()) {
            optionList.add(favouritePlace.getPlaceName());
        }
        return optionList;
    }

    public ArrayList<String> createSelectedOptionInfoList() {
        ArrayList<String> selectedOptionInfoList = new ArrayList<>();
        String selectedOption = "";

        for (FavouritePlace favouritePlace : MainActivity.dbHandler.getAllFavouritePlaces()) {
            selectedOption = "Wybrano: " + favouritePlace.getPlaceName();
            selectedOptionInfoList.add(selectedOption);
        }
        return selectedOptionInfoList;
    }
}
