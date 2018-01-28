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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import luocaca.rxretrofit.http.JsoupUtil;

/**
 * 视频 列表 预览 界面
 * <p>
 * <p>
 * 猫咪  网页图片
 * https://www.661cf.com/htm/index.htm
 */

public class MaoMiItemActivity extends BaseActivity {


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
    private static final String TAG = "MaoMiItemActivity";

    ProgressDialog progressDialog;

    @BindView(R.id.recycle_video_list)
    RecyclerView recyclerView;


    //    public String initUrl = "http://www.sssxx49.com/htm/index3.html";
//    public static String initUrl = "http://www.sssxx49.com/htm/index6.html";
    public static String initUrl = "https://www.661cf.com/htm/piclist1";
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
                        if (TextUtils.isEmpty(nextUrl)) {
                            Toast.makeText(MaoMiItemActivity.this, "到底了", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    }
                }
            }
        });

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
                        Document page = JsoupUtil.getMaoMiHtmlPage(mUrl);
//                        Document page = Jsoup.connect(mUrl).get();
                        Elements tds = page.getElementsByTag("td");
//      <td>
//              <a href="/htm/girllist10/" target="_blank">
//                    <img src="https://gg.385gg.com/girl/推女郎写真.gif" height="300" width="235">
//              </a>
//      </td>


                        for (int i = 0; i < tds.size(); i++) {
                            Movie movie = new Movie();
//                            String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
//                            String href = tds.get(i).select("a").attr("abs:href");

                            String href = tds.get(i).select("a").attr("abs:href");
                            String text = tds.get(i).select("img").attr("src");
                            String head = tds.get(i).select("img").attr("src");
                            movie.url = href;
                            movie.title = text;
                            movie.headImg = head;

//                            String text = "类型";
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onNext: " + e.getMessage());
                        Toast.makeText(mActivity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");

                    }
                })
        ;

//                .map(new Function<String, ArrayList<Movie>>() {
//                    @Override
//                    public ArrayList<Movie> apply(String s) throws Exception {
//                        String jsonCache = getCacheString(s);
//                        if (TextUtils.isEmpty(jsonCache)) {
//                            return null;
//                        } else {
//                            ArrayList<Movie> movieList1 = new Gson().fromJson(jsonCache, new TypeToken<ArrayList<Movie>>() {
//                            }.getType());
//
//                            return movieList1;
//                        }
//                    }
//                }).flatMap(new Function<ArrayList<Movie>, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(ArrayList<Movie> movies) throws Exception {
//
//
//                return null;
//            }
//        });


//        request();

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
                    MaoMiItemTypeActivity.initUrl = urlList.get(position).url;

                    mContext.startActivity(new Intent(mContext, MaoMiItemTypeActivity.class));
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
