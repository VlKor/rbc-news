package develop.test.ru.rbknews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by Владимир on 19.09.2015.
 */
public class DashboardActivity extends Activity {
    Button newsListBtn;
    Button settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);

        newsListBtn = (Button) findViewById(R.id.news_list_button);
        settingsBtn = (Button) findViewById(R.id.settings_button);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.news_list_button :
                        Intent intentNewsList = new Intent(DashboardActivity.this, NewsListActivity.class);
                        startActivity(intentNewsList);
                        break;
                    case R.id.settings_button :
                        Intent intentSettings = new Intent(DashboardActivity.this, SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
            }
        };

        newsListBtn.setOnClickListener(onClickListener);
        settingsBtn.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
