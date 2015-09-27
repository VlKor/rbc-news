package develop.test.ru.rbknews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Владимир on 24.09.2015.
 */
public class SettingsActivity extends Activity {
    RadioGroup radioGroupCategoryOfNews;
    RadioGroup radioGroupFrequencyOfUpdating;

    public static final String APP_PREFERENCES = "mysettings";
    final String KEY_CATEGORY_NEWS_RADIOBUTTON_INDEX = "SAVED_CATEGORY_NEWS_RADIOBUTTON_INDEX";
    final String KEY_FREQUENCY_UPDATING_RADIOBUTTON_INDEX = "SAVED_FREQUENCY_UPDATING_RADIOBUTTON_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        radioGroupCategoryOfNews = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroupFrequencyOfUpdating = (RadioGroup) findViewById(R.id.rGFrequencyOfUpdating);
        radioGroupCategoryOfNews.setOnCheckedChangeListener(radioGroupCategoryOnCheckedChangeListener);
        radioGroupFrequencyOfUpdating.setOnCheckedChangeListener(radioGroupUpdatingOnCheckedChangeListener);
        LoadPreferences();
    }

    RadioGroup.OnCheckedChangeListener radioGroupCategoryOnCheckedChangeListener =
            new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton checkedRadioButton = (RadioButton) radioGroupCategoryOfNews
                    .findViewById(checkedId);
            int checkedIndex = radioGroupCategoryOfNews.indexOfChild(checkedRadioButton);

            SavePreferences(KEY_CATEGORY_NEWS_RADIOBUTTON_INDEX, checkedIndex);
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupUpdatingOnCheckedChangeListener =
            new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checkedRadioButton = (RadioButton) radioGroupFrequencyOfUpdating
                            .findViewById(checkedId);
                    int checkedIndex = radioGroupFrequencyOfUpdating.indexOfChild(checkedRadioButton);

                    SavePreferences(KEY_FREQUENCY_UPDATING_RADIOBUTTON_INDEX, checkedIndex);
                }
            };

    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        int savedCategoryRadioIndex = sharedPreferences.getInt(KEY_CATEGORY_NEWS_RADIOBUTTON_INDEX, 0);
        int savedFrequencyRadioIndex = sharedPreferences.getInt(KEY_FREQUENCY_UPDATING_RADIOBUTTON_INDEX, 0);

        RadioButton savedCategoryRadioButton = (RadioButton) radioGroupCategoryOfNews
                .getChildAt(savedCategoryRadioIndex);
        savedCategoryRadioButton.setChecked(true);

        RadioButton savedFrequencyRadioButton = (RadioButton) radioGroupFrequencyOfUpdating
                .getChildAt(savedFrequencyRadioIndex);
        savedFrequencyRadioButton.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.up :
                Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
