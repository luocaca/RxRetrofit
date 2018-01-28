package luocaca.rxretrofit.http;

import android.graphics.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/28 0028.
 */

public class JsoupUtil {


    public static Document getMaoMiHtmlPage(String mUrl) throws IOException {

        ArrayList<Movie> arrayList = new ArrayList();
        Map<String, String> map = new HashMap<>();
        map.put("Hm_lvt_767e27c6fc5a7b6a90ba665ed5f7559b", "1517116132");

        map.put("Hm_lpvt_767e27c6fc5a7b6a90ba665ed5f7559b", "1517119390");
        map.put("_gid", "GA1.2.732345019.1517116133");
        map.put("_gat_gtag_UA_108266294_3", "1");
        map.put("_ga", "GA1.2.1141557328.1517116133");

        map.put("__cfduid", "d0e2a0843faa1aab0bf5485a44b26a1e71517118958");

        Map<String, String> heads = new HashMap<>();

        map.put("_ga", "GA1.2.1141557328.1517116133");

        Document page = Jsoup.connect(mUrl).cookies(map)
                .header("host", "www.657cf.com")
                .header("Referer", "https://www.657cf.com/")
                .get();
        return page;
    }


}
