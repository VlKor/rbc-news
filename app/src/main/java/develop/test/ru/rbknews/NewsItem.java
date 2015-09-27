package develop.test.ru.rbknews;

import org.jsoup.nodes.Element;

/**
 * Created by Владимир on 23.09.2015.
 */
public class NewsItem {
    private String newsHeader;
    private String newsHref;

    protected NewsItem(Element news) {
        this.newsHeader = news.text();
        this.newsHref = news.getElementsByAttribute("href").get(0).attributes().get("href");
    }

    protected String getNewsHeader() {
        return newsHeader;
    }

    protected String getNewsHref() {
        return newsHref;
    }
}
