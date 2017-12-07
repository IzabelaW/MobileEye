package mobileeye.mobileeye.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mobileeye.mobileeye.R;

/**
 * Created by Nexito on 05.12.2017.
 */

public class ConfigurationActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

        context=this;

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configurationIntent = new Intent(context, AddFavouritiesActivity.class);
                startActivityForResult(configurationIntent,2);
            }
        });


        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configurationIntent1 = new Intent(context, EditFavouritiesActivity.class);
                startActivityForResult(configurationIntent1,2);
            }
        });


        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent configurationIntent2 = new Intent(context, AddFavouritiesActivity.class);
                startActivityForResult(configurationIntent2,2);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Images.setCurrentMenu(Constants.MAIN_MENU);
        super.onBackPressed();

    }
}