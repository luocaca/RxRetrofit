package luocaca.rxretrofit.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.bean.ApiResult;
import luocaca.rxretrofit.di.component.DaggerMainActivityComponent;
import luocaca.rxretrofit.di.module.ClientModel;
import luocaca.rxretrofit.http.Api;
import luocaca.rxretrofit.http.JsonUtil;
import luocaca.rxretrofit.ui.maomi.MaoMiActivity;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private String appkey = "2e064c307344797872c0b22459ee4c1f";


    @BindView(R.id.toolbar)
    public Toolbar toolbar;




    @Inject
    Retrofit retrofit;


    private Api api;

    String text = " player: <div class=\"player\">\n" +
            "    <div id=\"a1\"></div>\n" +
            "    <script type=\"text/javascript\" src=\"/ckplay/ckplayer.js\"></script> \n" +
            "    <script type=\"text/javascript\">\n" +
            "       var flashvars={\n" +
            "           f:' \n" +
            "\t\t   https://dns.63mimi.com/20170913/146/1/xml/91_d0c48598f3ad41cd9cb028ef8f44abeb.mp4\n" +
            "\t\t   ',\n" +
            "            c:0,\n" +
            "           i:'http://201709.www05ruru.com:8888/mb1/gg/dizhi.jpg',\n" +
            "           e:1,\n" +
            "           l:'http://201709.www05ruru.com:8888/mb1/gg/pc.jpg',\n" +
            "           r:'http://201709.www05ruru.com:8888/mb1/gg/dytz.html',\n" +
            "           t:10,\n" +
            "           d:'http://201709.www05ruru.com:8888/mb1/gg/gg.gif',\n" +
            "       };    var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};\n" +
            "       video=\n" +
            "\t   [\"\n" +
            "\t   https://dns.63mimi.com/20170913/146/1/xml/91_d0c48598f3ad41cd9cb028ef8f44abeb.mp4 \n" +
            "\t   ->\n" +
            "\t   video/mp4\"];\n" +
            "       CKobject.embed('/ckplay/ckplayer.swf','a1','ckplayer_a1','610','485',false,flashvars,video,params);\n" +
            "   </script>\n" +
            "   </div>";
    private int userId = 1311110;


    @Override
    public void initView(Bundle savedInstanceState) {


        DaggerMainActivityComponent.builder()
                .clientModel(new ClientModel())
                .build()
                .inject(this);


//        {"code":"200","message":"success","data":{"userid":1311111,"vip":"2","livevip":"1","credit":"0"}}
        userId = 1311510;

//        1311111
        Log.i("live", "retrofit");

//        for (int i = 0; i < 100; i++) {
//            Log.i(TAG, "initView: " + 1311110);
//            userId++;
//
//            retrofit.create(Api.class)
//                    .requestVip("http://api.nlkmlk.com:81/love/user/" + userId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Result>() {
//                        @Override
//                        public void accept(Result result) throws Exception {
//                            if (result.data.vip > 0 || result.data.livevip > 0) {
//                                Log.i(TAG, "accept: " + result.toString());
//                            }
//                        }
//                    });
//
//        }


//        Log.i("live", "retrofit");
//        retrofit.create(Api.class)
//                .requestVip("http://api.nlkmlk.com:81/love/user/2091002")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Result>() {
//                    @Override
//                    public void accept(Result result) throws Exception {
//                        Log.i(TAG, "accept: " + result.toString());
//                    }
//                });

        String result = text.substring(text.indexOf("http"), text.indexOf(".mp4") + 4);

        //http://api.nlkmlk.com:81/love/user/2091002


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("live", "开始get请求");
//                String re = "";
//                try {
//                    Log.i("live", "initView: ");
//                    re = GetUtil.get("http://api.nlkmlk.com:81/love/user/2091002");
//                    Log.i("live", "http://api.nlkmlk.com:81/love/user/2091002 \n" + re);
//                } catch (IOException e) {
//                    Log.i("live", "initView: result" + e.getMessage());
//                    e.printStackTrace();
//                }
//                Log.i("live", "结束get请求");
//            }
//        }).start();


//        Toast.makeText(mActivity, "result\n" + result, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "result \n: " + result);

        verifyStoragePermissions(mActivity);


        setSupportActionBar(toolbar);

//        postAdd();


//        getHtmlRes();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                //获取登录信息。并且保存住请求cokie
//
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("password", "gcSZtaXMPUUrDs9YBXzroQaph4mhIB/rEmkwlwNQHSUhOlRxBkH65hVjWje41Vy9FDeawfWwtXfMbY/suBAPPkaGj+3JPk+k7OsJRwzDhOuEKH2hOvYC1Z3ihqDKIElv4gOzuAjddHMH6tpZPOPcq6qIJKAPzFwzfodfKg7Wv+s="
//                );
//                params.put("username", "17074990702");
//                params.put("remember", "true");
//
//                String hos = "http://123.207.176.15/user/login?version=3.5.0&platform=android&packageId=3&channel=and-laosiji.cpd-3&deviceName=HUAWEI+FRD-AL00&androidVersion=7.0";
//                String cookie = null;
//                try {
//                    cookie = PostUtil.post(hos, null, params, null);
//                } catch (IOException e) {
//                    cookie = "";
//                    e.printStackTrace();
//                }
//
//
//                getRoomMsg(cookie);
//
//            }
//        }).start();


    }

    private void getRoomMsg(String cookie) {

//        POST /room/getRooms?page=0&status=1&version=3.5.0&platform=android&packageId=3 HTTP/1.1
//        connection: keep-alive
//        Charsert: UTF-8
//        Content-Type: multipart/form-data;boundary=96b49f79-cc3d-414a-85ce-f8f25ddbe283
//        User-Agent: Dalvik/2.1.0 (Linux; U; Android 7.0; FRD-AL00 Build/HUAWEIFRD-AL00)
//        Host: 123.207.176.15
//        Accept-Encoding: gzip
//        Content-Length: 42

        String host = "http://123.207.176.15/room/getRooms?page=0&status=1&version=3.5.0&platform=android&packageId=3";

        HttpURLConnection connection;
        try {

            URL url = new URL(host);


            connection = (HttpURLConnection) url.openConnection();


            if (!TextUtils.isEmpty(cookie)) {
                connection.setRequestProperty("Cookie", cookie);
            }
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoInput(true);


            connection.connect();


            int code = connection.getResponseCode();

            if (code == 200) {
                StringBuffer buffer = new StringBuffer();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

//                byte[] b = new byte[1024];
//                int length;
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }

//                while ((length = inputStream.read(b)) != -1) {
//                    length = inputStream.read(b);
//                    buffer.append(new String(b));
//                    Log.i(TAG, "getRoomMsg: " + length);
//                }
                Log.i(TAG, "getRoomMsg: " + buffer.toString());

                String json = buffer.toString();

                Log.i(TAG, "getRoomMsg: " + json);


                Log.i(TAG, "getRoomMsg: " + JsonUtil.formatJson(json));

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getHtmlRes(String url) {

//        String url = "http://image.baidu.com/n/pc_list?queryImageUrl=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D511769507%2C3839834658%26fm%3D27%26gp%3D0.jpg&querySign=511769507,3839834658&query=%E5%9B%BE%E7%89%87%20%E9%A3%8E%E6%99%AF&srctype=0&fp=searchdetail&pos=card&fm=searchdetail&uptype=button&objtype=0#activeTab=3";
//        String url = "http://www.hmeg.cn/index";


//        String url = "http://www.sssxx49.com/html/mode/index2473.html";
//        String url = "http://www.sssxx49.com/html/mode/index1978.html";

        Toast.makeText(this, "开始获取html", Toast.LENGTH_SHORT).show();

        try {
//            String url = "http://xjh.haitou.cc/wh/uni-1/after/hold/page-1/";
            Connection conn = Jsoup.connect(url);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
            Document doc = conn.get();

            // 获取tbody元素下的所有tr元素
//            Elements elements = doc.select("tbody tr");

            //获取后缀为png和jpg的图片的元素集合
            Elements pngs = doc.select("img[src~=(?i)\\.(png|jpe?g)]");

            for (Element element : pngs) {


                String src = element.attr("src");//获取img中的src路径
                //获取后缀名
                String imageName = src.substring(src.lastIndexOf("/") + 1, src.length());


                postAdd(src);
//                String companyName = element.getElementsByTag("company").text();
//                String time = element.select("td.text-center").first().text();
//                String address = element.getElementsByClass("preach-tbody-addre").text();
//
                Log.i(TAG, "src：" + src);
                Log.i(TAG, "imageName：" + imageName);


//                Log.i(TAG, "宣讲时间：" + time);
//                Log.i(TAG, "宣讲学校：华中科技大学");
//                Log.i(TAG, "具体地点：" + address);
//                Log.i(TAG, "---------------------------------");
            }
        } catch (IOException e) {
            Toast.makeText(this, "网页获取错误", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }


        Toast.makeText(this, "结束获取html", Toast.LENGTH_SHORT).show();


    }

    @Override
    public int BindLayoutID() {
        return R.layout.activity_main;
    }


    private void postAdd(String imgUrl) {

//        Retrofit retrofit = RetrofitManager.create();

        int dd1 = (int) (Math.random() * 10000);
//        long dd = System.currentTimeMillis();
//        Toast.makeText(this, "" + dd * 1000, Toast.LENGTH_SHORT).show();

        Observable<ApiResult> observable = api.requestAdd("" + dd1 * 1000, "" + dd1 * 1000, "retrofit", "retrofit", imgUrl);
        observable.subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ApiResult>() {
                    @Override
                    public void accept(ApiResult city) throws Exception {


                        Log.i(TAG, "succeed: ");
                        Toast.makeText(MainActivity.this, "" + city.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "faild: ");

                    }
                })
        ;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
// <li><a href="/htm/index2.html">亚洲风情</a></li>
//             <li><a href="/htm/index3.html">强奸乱伦</a></li>
//             <li><a href="/htm/index4.html">网络直播</a></li>
//             <li><a href="/htm/index5.html">日韩女忧</a></li>
//             <li><a href="/htm/index6.html">偷拍自拍</a></li>
//             <li><a href="/htm/index7.html">欧美激情</a></li>
//             <li><a href="/htm/index8.html">卡通动漫</a></li>
//             <li><a href="/htm/index9.html">学生影片</a></li>

    @OnClick({R.id.button, R.id.fab, R.id.button3, R.id.yazhou,
            R.id.zipai, R.id.oumei, R.id.fenlu, R.id.weimei,
            R.id.siwa,
            R.id.guochang
            , R.id.yzfq
            , R.id.qjll,
            R.id.wlzb,
            R.id.rhny,
            R.id.tpzp,
            R.id.omjq
            , R.id.ktdm,
            R.id.xsyp,
            R.id.show_books


    })
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.show_books:
                startActivity(new Intent(mActivity, ShowVideoActivity.class));
/*view-source:http://www.sssxx49.com/html/aky/index20.html   图书*/




                //图书界面
                break;

            case R.id.button:
                startActivity(new Intent(MainActivity.this, BookListActivity.class));
                break;

            case R.id.guochang:
                Log.i(TAG, "onClick: " + UR_Pic.guochang.mUrl);
                PageJsoupActivity.initUrl = UR_Pic.guochang.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            case R.id.yazhou:
                Log.i(TAG, "onClick: " + UR_Pic.yazhou.mUrl);
                PageJsoupActivity.initUrl = UR_Pic.yazhou.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            case R.id.zipai:
                PageJsoupActivity.initUrl = UR_Pic.zipai.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;

            case R.id.oumei:
                PageJsoupActivity.initUrl = UR_Pic.oumei.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            case R.id.fenlu:
                PageJsoupActivity.initUrl = UR_Pic.fenlu.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            case R.id.weimei:
                PageJsoupActivity.initUrl = UR_Pic.weimei.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;

//            case R.id.yzfq:
//                VideoListActivity.initUrl = UR_Pic.yzfq.mUrl;
//                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
//                break;
            case R.id.qjll: //
                VideoListActivity.initUrl = UR_Pic.qjll.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;
            case R.id.wlzb:
                VideoListActivity.initUrl = UR_Pic.wlzb.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;
            case R.id.rhny:
                VideoListActivity.initUrl = UR_Pic.rhny.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;
            case R.id.tpzp:
                VideoListActivity.initUrl = UR_Pic.tpzp.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;
            case R.id.omjq:
                VideoListActivity.initUrl = UR_Pic.omjq.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;
            case R.id.ktdm:
                VideoListActivity.initUrl = UR_Pic.ktdm.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;



            case R.id.yzfq:
                VideoListActivity.initUrl = UR_Pic.yzfq.mUrl;
                startActivity(new Intent(MainActivity.this, VideoListActivity.class));
                break;


            case R.id.siwa:
                PageJsoupActivity.initUrl = UR_Pic.siwa.mUrl;
                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            case R.id.button3:

                startActivity(new Intent(MainActivity.this, PageJsoupActivity.class));
                break;


            /**
             *  <li><a href="/htm/index2.html">亚洲风情</a></li>
             <li><a href="/htm/index3.html">强奸乱伦</a></li>
             <li><a href="/htm/index4.html">网络直播</a></li>
             <li><a href="/htm/index5.html">日韩女忧</a></li>
             <li><a href="/htm/index6.html">偷拍自拍</a></li>
             <li><a href="/htm/index7.html">欧美激情</a></li>
             <li><a href="/htm/index8.html">卡通动漫</a></li>
             <li><a href="/htm/index9.html">学生影片</a></li>
             */


            /*猫咪*/
            case R.id.xsyp:
//                VideoListActivity.initUrl = UR_Pic.xsyp.mUrl;
                startActivity(new Intent(MainActivity.this, MaoMiActivity.class));
                break;

            case R.id.fab:

                //跳转天天 lu
                String url = "http://www.2013sss.com/wap/";
//
                mActivity.startActivity(new Intent(mActivity, TTLActivity.class));
//                for (int i = 0; i < 100; i++) {
//                    Log.i(TAG, "initView: " + userId);
//                    userId++;
//
//                    retrofit.create(Api.class)
//                            .requestVip("http://api.nlkmlk.com:81/love/user/" + userId)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<Result>() {
//                                @Override
//                                public void accept(Result result) throws Exception {
//                                    if (result.data.vip > 0 || result.data.livevip > 0) {
//                                        Log.i(TAG, "accept: " + result.toString());
//                                    }
//                                }
//                            });
//                }


//                postAdd();

//                ACache.get(mActivity).clear();


//                Observable.just("http://www.sssxx49.com/html/mode/index1295.html")
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                getHtmlRes(s);
//                            }
//                        });


//                Snackbar.make(view, "清除图片缓存", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                break;
        }

    }


    public enum UR_Pic {

        //http://www.sssxx49.com/html/mode/index2647.html
//        yazhou("http://www.sssxx49.com/html/aky/index40.html"),
        yazhou("http://www.sssxx49.com/html/mode/index2759.html"),
        //        zipai("http://www.sssxx49.com/html/aky/index41.html"),
        zipai("http://www.sssxx49.com/html/mode/index2745.html"),

        oumei("http://www.sssxx49.com/html/mode/index2764.html"),
        fenlu("http://www.sssxx49.com/html/mode/index2762.html"),
        guochang("http://www.sssxx49.com/html/mode/index2750.html"),
        weimei("http://www.sssxx49.com/html/mode/index2774.html"),
        siwa("http://www.sssxx49.com/html/aky/index46.html"),
        katong("http://www.sssxx49.com/html/aky/index47.html"),

        yzfq("http://www.sssxx49.com/htm/index2.html"),
        qjll("http://www.sssxx49.com/htm/index3.html"),
        wlzb("http://www.sssxx49.com/htm/index4.html"),
        rhny("http://www.sssxx49.com/htm/index5.html"),
        tpzp("http://www.sssxx49.com/htm/index6.html"),
        omjq("http://www.sssxx49.com/htm/index7.html"),
        ktdm("http://www.sssxx49.com/htm/index8.html"),
        xsyp("http://www.sssxx49.com/htm/index9.html");

/*view-source:http://www.sssxx49.com/html/aky/index20.html   图书*/


        public String mUrl;

        UR_Pic(String url) {
            mUrl = url;
        }

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
