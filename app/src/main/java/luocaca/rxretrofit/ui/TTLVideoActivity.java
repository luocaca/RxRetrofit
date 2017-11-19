package luocaca.rxretrofit.ui;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.App;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.cache.ACache;

/**
 * 用来播放视频 ttl 的界面
 */

public class TTLVideoActivity extends BaseActivity {


    private static final String TAG = "TTLVideoActivity";

    public static String title = "大傻么么哒";

    public static String initUrl = "http://www.sssxx49.com/modo/index1932.html";
//    public static String initUrl = "http://www.sssxx49.com/htm/index3.html";
    //https://d2.xia12345.com/down/2017/8/27001/17826124.mp4

    //    String str = "https://d1.xia12345.com/down/201708/08/pt124.mp4";
    String str = "https://d2.xia12345.com/down/2017/8/27001/17826124.mp4";
    String str1 = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;

    JCVideoPlayerStandard jcVideoPlayerStandard;
    private String staticUrl;

    @Override
    public void initView(Bundle savedInstanceState) {

        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("正在获取视频地址地址");
        dialog.show();
        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        //SCREEN_LAYOUT_NORMAL
        jcVideoPlayerStandard.thumbImageView.setBackgroundResource(R.drawable.jc_bottom_bg);


//        staticUrl = "http://www.sssxx49.com/play/2043-0-0.html";
        staticUrl = "http://ugcbsy.qq.com/flv/153/109/c01350046ds.p702.1.mp4?sdtfrom=v1010&guid=fa465656cc6dba47f514d75a833e2b5c&vkey=8B3D3C957F42F62B223A92FFD0EE8F4FB8CEA5EE3C52A4E8EE4E67F784B2EE10F4DB1AF0591FF205D6A002D66A1C07266C4C931AA2F9DD4865017264DCA4ABEDFD7C34003D725D7FD4AE34A3257CD0422049B3422EA62ED5505105B4E5D7F25BC3D468EFCF63817B38D0FBCC48B7A421997A4EE38AB27536";

        //txt
        Observable.just(initUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String url) throws Exception {
                        String cache_url = ACache.get(mActivity).getAsString(url);
                        if (TextUtils.isEmpty(cache_url)) {
                            Log.i(TAG, "获取缓存地址失败。。。。。执行线程，，获取播放地址");
                            return true;
                        } else {
                            Log.i(TAG, "获取缓存地址成功。。。。。执行线程，，获取播放地址\n" + cache_url);
                            openVideo(cache_url);
                            return false;
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String url) throws Exception {
                        Log.i(TAG, "accept: 开始加载........" + url);

                        getMovieUrl(url);
                    }
                });


    }


    ProgressDialog dialog;

    public void openVideo(String str) {

        Log.i(TAG, "获取播放地址，进行缓存 ，，，，播放地址对应播放界面地址");
        Log.i(TAG, initUrl + "-->" + str);

        ACache.get(mActivity).put(initUrl, str);

        Log.i(TAG, "openVideo: " + str);

        dialog.dismiss();
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(str);
        Log.i(TAG, "openVideo:........ " + str);
        Log.i(TAG, "proxyUrl:........ " + proxyUrl);
        jcVideoPlayerStandard.setUp(proxyUrl
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
//        sensorPortrait
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "大傻么么哒");
        jcVideoPlayerStandard.startVideo();
        //setImage();
        //"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"

    }

    private HttpProxyCacheServer getProxy() {
        // should return single instance of HttpProxyCacheServer shared for whole app.
        return App.getProxy(mActivity);


    }


    public void getMovieUrl(String mUrl) {

        //http://www.2013sss.com/view/index33410.html  播放地址
        //<a href="/player/index33410.html?33410-0-0" class="txt"><img src="..//template/1/images/20131051312281581.gif"></a>
        // mp4 地址   通过上面这个获取


        try {
            Document document = Jsoup.connect(mUrl).get();

            String realUrl = document.getElementsByClass("txt").first().attr("abs:href");
            Toast.makeText(mActivity, "realUrl" + realUrl, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "getMovieUrl: " + realUrl);
            Document realPage = Jsoup.connect(realUrl).get();

            Elements elements = realPage.getElementsByClass("play_left");
            Elements player = realPage.getElementsByClass("player");
            Log.i(TAG, "getMovieUrl: " + elements.text());
            Log.i(TAG, "getMovieUrl: " + elements);
            Log.i(TAG, "player: " + player);

            String resul = zhengze(player.toString());


            //<video controls="controls"
            // src="https://dns.63mimi.com/20171103/1/1/xml/91_dd4da15a80f545ecfd745bdeb16bd785.mp4"
            // id="ckplayer_a1" width="610" height="485" loop="loop"
            // poster="http://201709.www05ruru.com:8888/mb1/gg/dizhi.jpg"
            // style="display: none !important;">
            // </video>

            Element mp4UrlEl = realPage.getElementsByClass("controls").first();
            Element video = realPage.getElementsByTag("video").first();

            Log.i(TAG, "getMovieUrl: " + mp4UrlEl);
            Log.i(TAG, "video: " + video);
//            Document document = Jsoup.connect(mUrl).get();

//            String movieUrl = elements.first().select("a").attr("href");


            Observable.just(resul)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String videoUrl) throws Exception {
                            openVideo(videoUrl);
                        }
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }


        /**
         * <script type="text/javascript">document.writeln(vodhelp1);</script>

         <div class="film_bar clearfix">

         <span>下载地址：</span>
         <b><font color="red">点击或复制以下链接到任意下载工具进行下载！</font></b>
         <ul>

         <li>
         <a title='https://d2.xia12345.com/down/2017/8/27001/17826124.mp4'
         href='https://d2.xia12345.com/down/2017/8/27001/17826124.mp4'
         target="_blank">https://d2.xia12345.com/down/2017/8/27001/17826124.mp4
         </a>
         </li>

         </ul>

         </div>



         */

    }

    private String zhengze(String text) {
//        Pattern pattern = Pattern
//                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
//        System.out.println(pattern.matcher(url1).matches());
        String result = "";
        result = text.substring(text.indexOf("http"), text.indexOf(".mp4") + 4);
        Log.i(TAG, "zhengze: " + result);
//        String result = text.substring(text.indexOf("http"), text.indexOf(".mp4")+4);
        return result;
//        Toast.makeText(mActivity, "result\n" + result, Toast.LENGTH_SHORT).show();
//        Log.i(TAG, "result \n: " + result);


    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public int BindLayoutID() {
        return R.layout.activity_video;
    }


    public static class MyFileNameGenerator implements FileNameGenerator {

        // Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
        // e. g. http://example.com?videoId=abcqaz&sessionToken=xyz987
        public String generate(String url) {
            Uri uri = Uri.parse(url);
            String videoId = uri.getQueryParameter("videoId");
            return videoId + ".mp4";
        }
    }


}
