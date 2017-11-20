package mobileeye.mobileeye.activity;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import mobileeye.mobileeye.MenuDisplayer;

/**
 * Created by Nexito on 19.11.2017.
 */

public class OptionsClass implements View.OnLongClickListener,View.OnClickListener{

    private int currentOption=0;
    private  static TextView optionTextView;
    private MenuDisplayer menuDisplayer;
    private static String optionList[];
    private static String[] selectedOptionInfoList;
    private static OptionsSelection optionsSelection;

    public OptionsClass(MenuDisplayer menuDisplayer,TextView optionTextView ){

        this.menuDisplayer=menuDisplayer;
        this.optionTextView=optionTextView;
        optionTextView.setOnClickListener(this);
        optionTextView.setOnLongClickListener(this);
        //optionTextView.setOnDragListener(this);
    }

    public static void initializeOptions(String optionListT[],String[] selectedOptionInfoListT,OptionsSelection optionsSelectionT){
        optionList=optionListT;
        selectedOptionInfoList=selectedOptionInfoListT;
        optionsSelection=optionsSelectionT;


    }

    @Override
    public void onClick(View view) {

        currentOption++;
        if(currentOption >= optionList.length) {
            currentOption = 0;
        }

        optionTextView.setText(optionList[currentOption]);
        menuDisplayer.display(optionList[currentOption]);
    }

    @Override
    public boolean onLongClick(View view) {
        menuDisplayer.display(selectedOptionInfoList[currentOption]);
        optionsSelection.optionSelected(currentOption);

        return false;
    }


    public boolean onDrag(View view, DragEvent dragEvent) {
        optionTextView.setText("dragged");
        return false;
    }
}
