package luocaca.rxretrofit.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class JsoupPageBean implements Serializable {

    public String lastUrl = "";
    public String nextUrl = "";
    public String title = "";
    public List<String> imageList = new ArrayList<>();
    public String currentUrl = "";


    @Override
    public String toString() {
        return "JsoupPageBean{" +
                "lastUrl='" + lastUrl + '\'' +
                ", nextUrl='" + nextUrl + '\'' +
                ", title='" + title + '\'' +
                ", imageList=" + imageList +
                ", currentUrl='" + currentUrl + '\'' +
                '}';
    }
}
