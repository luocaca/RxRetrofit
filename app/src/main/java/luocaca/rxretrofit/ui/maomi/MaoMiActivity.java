package luocaca.rxretrofit.ui.maomi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.cache.ACache;

/**
 * 视频 列表 预览 界面
 * <p>
 * <p>
 * 猫咪  网页图片
 * https://www.661cf.com/htm/index.htm
 */

public class MaoMiActivity extends BaseActivity {


    /**
     * </div>-->
     * <div class="Nav">
     * <ul class="n_Box" style="height:48px;">
     * <li><a href="/" target="_self" class="a2">天天撸</a></li>
     * <p>
     * <li><a href=/list/index27.html>乱伦</a></li>
     * <p>
     * <li><a href=/list/index28.html>人妻</a></li>
     * <p>
     * <li><a href=/list/index29.html>偷拍</a></li>
     * <p>
     * <li><a href=/list/index34.html>学生</a></li>
     * <p>
     * <li><a href=/list/index54.html>巨乳</a></li>
     * <p>
     * <li><a href=/list/index55.html>日韩</a></li>
     * <p>
     * <li><a href=/list/index56.html>欧美</a></li>
     * <p>
     * <li><a href=/list/index57.html>国产</a></li>
     * <p>
     * <li><a href=/list/index59.html>无码AV</a></li>
     * <p>
     * <li><a href=/list/index60.html>制服</a></li>
     * <p>
     * </div>
     */


    //http://www.2013sss.com/list/index34.html
    private static final String TAG = "TTLActivity";

    ProgressDialog progressDialog;

    @BindView(R.id.recycle_video_list)
    RecyclerView recyclerView;

    @BindView(R.id.web)
    WebView web;


    //    public String initUrl = "http://www.sssxx49.com/htm/index3.html";
//    public static String initUrl = "http://www.sssxx49.com/htm/index6.html";
    public static String initUrl = "https://www.661cf.com/htm/index.htm";
    //https://d2.xia12345.com/down/2017/8/27001/17826124.mp4

    //    String str = "https://d1.xia12345.com/down/201708/08/pt124.mp4";
    String str = "https://d2.xia12345.com/down/2017/8/27001/17826124.mp4";
    String str1 = "http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4";


    public String nextUrl = "";
    List<Movie> movieList = new ArrayList<>();
    private Disposable mDisposable;

    @Override
    public void initView(Bundle savedInstanceState) {


        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("加载中.....");
//        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!mActivity.isFinishing() && mDisposable != null) {
//                    Toast.makeText(mActivity, "取消了网络请求", Toast.LENGTH_SHORT).show();
//                    mDisposable.dispose();
//                    mDisposable = null;
                }
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1));
        recyclerView.setAdapter(new MyAdapter(movieList, mActivity));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisiblePosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisiblePosition >= recyclerView.getLayoutManager().getItemCount() - 1) {

//                        System.out.println("====自动加载");
                        if (TextUtils.isEmpty(initUrl)) {
                            Toast.makeText(MaoMiActivity.this, "到底了", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(MaoMiActivity.this, "重新加载", Toast.LENGTH_SHORT).show();
                        request(initUrl);


                    }
                }
            }
        });


        if (getCacheList()!=null)
        {
            doRefresh(getCacheList());
        }
        else {

            request(initUrl);
        }

    }

    private void request(String initUrl) {
        Observable.just(initUrl)
                .subscribeOn(Schedulers.io())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .flatMap(new Function<String, ObservableSource<ArrayList<Movie>>>() {
                    @Override
                    public ObservableSource<ArrayList<Movie>> apply(String mUrl) throws Exception {
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
                        Elements lis = page.getElementsByClass("nav_menu clearfix").get(3).getElementsByTag("li");

                        for (int i = 0; i < lis.size(); i++) {
                            Movie movie = new Movie();
//                            String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
                            String href = lis.get(i).select("a").attr("abs:href");
                            String text = lis.get(i).select("a").text();
                            movie.url = href;
                            movie.title = text;
                            Log.i(TAG, "href: " + href + "   text:" + text);
                            arrayList.add(movie);
                        }

                        return Observable.just(arrayList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ArrayList<Movie>>() {
                    @Override
                    public void accept(ArrayList<Movie> movies) throws Exception {
                        //增加 二级 缓存  （本地缓存）  （中央缓存）

                    }
                })
                .subscribe(new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "Disposable: ");
                    }


                    @Override
                    public void onNext(ArrayList<Movie> value) {
                        Log.i(TAG, "onNext: " + value.toString());

                        doRefresh(value);

//                        web.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onNext: " + e.getMessage());
                        Toast.makeText(mActivity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                        //启用支持Javascript
                        WebSettings settings = web.getSettings();
                        settings.setJavaScriptEnabled(true);


//设置自适应

                        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL.SINGLE_COLUMN);

                        settings.setDefaultTextEncodingName("UTF-8");

                        settings.setAppCacheEnabled(true);

                        settings.setCacheMode(WebSettings.LOAD_DEFAULT);


                        web.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                Log.i(TAG, "shouldOverrideUrlLoading: ");
                                view.loadUrl(url);

                                return true;
                            }
                        });

                        web.loadUrl(initUrl);

                    }


                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");

                    }
                })
        ;

    }

    /**
     * </div>-->
     * <div class="Nav">
     * <ul class="n_Box" style="height:48px;">
     * <li><a href="/" target="_self" class="a2">天天撸</a></li>
     * <p>
     * <li><a href=/list/index27.html>乱伦</a></li>
     * <p>
     * <li><a href=/list/index28.html>人妻</a></li>
     * <p>
     * <li><a href=/list/index29.html>偷拍</a></li>
     * <p>
     * <li><a href=/list/index34.html>学生</a></li>
     * <p>
     * <li><a href=/list/index54.html>巨乳</a></li>
     * <p>
     * <li><a href=/list/index55.html>日韩</a></li>
     * <p>
     * <li><a href=/list/index56.html>欧美</a></li>
     * <p>
     * <li><a href=/list/index57.html>国产</a></li>
     * <p>
     * <li><a href=/list/index59.html>无码AV</a></li>
     * <p>
     * <li><a href=/list/index60.html>制服</a></li>
     * <p>
     * </div>
     */


    private void doRefresh(ArrayList<Movie> movies) {

        Log.i(TAG, "doRefresh: 刷新界面");
        movieList.clear();
        movieList.addAll(movies);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        web.setVisibility(View.GONE);
        add2Cache(movies);

    }

    public void add2Cache(ArrayList<Movie> movies) {
        ACache aCache = ACache.get(this);

        if (movies != null && movies.size() > 0) {

            Gson gson = new Gson();
            aCache.put("listJson", gson.toJson(movies));
        }


    }

    public ArrayList<Movie> getCacheList() {

        ACache aCache = ACache.get(this);
        ArrayList<Movie> movies = null;


        Gson gson = new Gson();
        String json = aCache.getAsString("listJson");


        Type type = new TypeToken<ArrayList<Movie>>() {
        }.getType();

        movies = gson.fromJson(json, type);
        return movies;
    }


    private void doRefresh(String jsonString) {
        ArrayList<Movie> movieList1 = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Movie>>() {
        }.getType());
        movieList.clear();
        movieList.addAll(movieList1);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    private ArrayList<Movie> getDataFromCache(String urlKey) {
        String json = mACache.getAsString(urlKey);
        if (TextUtils.isEmpty(json)) {
            Log.i(TAG, "acache 获取本地缓存失败，尝试获取中央缓存。。。。");


        }


        return null;
    }


    @Override
    public int BindLayoutID() {
        return R.layout.activity_video_list;
    }


    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        public List<Movie> urlList;
        public Context mContext;


        public MyAdapter(List<Movie> list, Context context) {
            urlList = list;
            mContext = context;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_list, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.title.setText(urlList.get(position).title + "\n" + urlList.get(position).url);
            holder.url.setText(urlList.get(position).url);

            if (!TextUtils.isEmpty(urlList.get(position).headImg)) {
                String head = urlList.get(position).headImg;


//                if (head.startsWith("http://104.167.24.3")) {
                if (head.split(":").length > 2) {
                    head = head.replace("http://104.167.24.3", "");
                }

                if (head.contains(" ")) {
                    head = head.replace(" ", "");

                }

                Log.i(TAG, urlList.get(position).title + "图片地址: " + head);

                Glide.with(mContext)
                        .load(head)
                        .placeholder(R.mipmap.maomi)
                        .error(R.drawable.jc_error_normal)
                        .into(holder.imageView);
            }

            ((ViewGroup) holder.imageView.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TTLVideoActivity.title = urlList.get(position).title;
                    MaoMiItemActivity.initUrl = urlList.get(position).url;

                    mContext.startActivity(new Intent(mContext, MaoMiItemActivity.class));
                }
            });

            Log.i("url", ": " + urlList.get(position).url);
            Log.i("headImg", ": " + urlList.get(position).headImg);
            Log.i("title", ": " + urlList.get(position).title);
        }


        @Override
        public int getItemCount() {
            return urlList.size();
        }
    }


    class MyHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView url;
        public ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            url = itemView.findViewById(R.id.url);
        }
    }


    class Movie implements Serializable

    {
        public String headImg = "";
        public String title = "";
        public String url = "";

        public String lastUrl = "";
        public String nextUrl = "";

        @Override
        public String toString() {
            return "Movie{" +
                    "headImg='" + headImg + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


    public Object getCacheObj(String key) {
        return ACache.get(mActivity).getAsObject(key);
    }

    public String getCacheString(String key) {
        return ACache.get(mActivity).getAsString(key);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDisposable != null) {
            Toast.makeText(mActivity, "取消请求", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onBackPressed: 取消请求");
            mDisposable.dispose();
            mDisposable = null;
        }
    }


    public void controlDialog(String msg) {
        Observable.just(msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String ms) throws Exception {
                        if (!mActivity.isFinishing()) {
                            progressDialog.setMessage(ms);
                            if (!progressDialog.isShowing())
                                progressDialog.show();
                        }
                    }
                });
    }

}
