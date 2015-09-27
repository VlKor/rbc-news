package develop.test.ru.rbknews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class NewsListActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    ListView listView;
    ParsingDataAsyncTask parsingDataAsyncTask;
    Document htmlDocument;
    ArrayList<String> listHeaderNews = new ArrayList<>();
    ArrayList<NewsItem> newsItemsList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    int savedCategoryRadioIndex;
    int savedFrequencyRadioIndex;
    ArrayAdapter<String> adapter;

    public static final String APP_PREFERENCES = "mysettings";
    final String KEY_CATEGORY_NEWS_RADIOBUTTON_INDEX = "SAVED_CATEGORY_NEWS_RADIOBUTTON_INDEX";
    final String KEY_FREQUENCY_UPDATING_RADIOBUTTON_INDEX = "SAVED_FREQUENCY_UPDATING_RADIOBUTTON_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_list);

        listView = (ListView) findViewById(R.id.newsListView);

        /* Getting selected radio button index */
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        savedCategoryRadioIndex = sharedPreferences.getInt(KEY_CATEGORY_NEWS_RADIOBUTTON_INDEX, 0);
        savedFrequencyRadioIndex = sharedPreferences.getInt(KEY_FREQUENCY_UPDATING_RADIOBUTTON_INDEX, 0);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_dark, android.R.color.holo_green_dark);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listHeaderNews);
        listView.setAdapter(adapter);

        /* fixme: Timer is not the best solution. Google recommends using ScheduledExecutorService.
                  I tried to use it, but it did not succeed. */
        Timer myTimer = new Timer();
        final Handler uiHandler = new Handler();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        populateNewsElements();
                    }
                });
            }
        }, 0L, calculateFrequencyUpdate(savedFrequencyRadioIndex));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String newsItemHref = newsItemsList.get(position).getNewsHref();
                Uri uri = Uri.parse(newsItemHref);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }



    /** Method receives a html document, parses it into elements with the necessary classes and
     * creates array of elements NewsItem. Also method fills array of header news.
     * @param
     * @return void
     * */
    protected void populateNewsElements() {
        /* check the connection availability */
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(null != activeNetwork) {
            parsingDataAsyncTask = new ParsingDataAsyncTask();
            parsingDataAsyncTask.execute(savedCategoryRadioIndex);
            try {
                htmlDocument = parsingDataAsyncTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            newsItemsList.clear();
            Elements newsElements = htmlDocument.getElementsByClass("announce__inner");
            for(org.jsoup.nodes.Element newsElement : newsElements) {
                NewsItem newsItem = new NewsItem(newsElement);
                newsItemsList.add(newsItem);
            }

            listHeaderNews.clear();
            for(NewsItem newsItem : newsItemsList) {
                listHeaderNews.add(newsItem.getNewsHeader());
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, R.string.error_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    /** Method receives the index of the selected radio button and calculated frequency to
     * update the list view
     * @param savedFrequencyRadioIndex - index of the selected radio button into the settings
     * @return frequency to update the list view
     * */
    protected Long calculateFrequencyUpdate(int savedFrequencyRadioIndex) {
        Long frequencyUpdate;
        switch (savedFrequencyRadioIndex) {
            case 0 :
                frequencyUpdate = 60L;
                break;
            case 1 :
                frequencyUpdate = 180L;
                break;
            case 2 :
                frequencyUpdate = 300L;
                break;
            case 3 :
                frequencyUpdate = 600L;
                break;
            default: frequencyUpdate = 60L;
        }
        return frequencyUpdate * 1000;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateNewsElements();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
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
                Intent intent = new Intent(NewsListActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
