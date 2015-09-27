package develop.test.ru.rbknews;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Владимир on 22.09.2015.
 */
public class ParsingDataAsyncTask extends AsyncTask<Integer, Void, Document> {
    String parsedUrl;

    @Override
    protected Document doInBackground(Integer... idCheckBox) {
        switch (idCheckBox[0].toString()) {
            case "0" :
                parsedUrl = "http://top.rbc.ru/politics/";
                break;
            case "1" :
                parsedUrl = "http://top.rbc.ru/economics/";
                break;
            case "2" :
                parsedUrl = "http://top.rbc.ru/finances/";
                break;
            case "3" :
                parsedUrl = "http://top.rbc.ru/business/";
                break;
            case "4" :
                parsedUrl = "http://top.rbc.ru/technology_and_media/";
                break;
            case "5" :
                parsedUrl = "http://top.rbc.ru/own_business/";
                break;
            case "6" :
                parsedUrl = "http://top.rbc.ru/money/";
                break;
            default: parsedUrl = "http://top.rbc.ru/politics/";
        }

        Document document = null;
        try {
            document = Jsoup.connect(parsedUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    @Override
    protected void onPostExecute(Document document) {
        super.onPostExecute(document);
    }
}
