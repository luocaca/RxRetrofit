package luocaca.rxretrofit.ui;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
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
 */

public class VideoListActivity extends BaseActivity {


    private static final String TAG = "VideoListActivity";

    ProgressDialog progressDialog;

    @BindView(R.id.recycle_video_list)
    RecyclerView recyclerView;

    //    public String initUrl = "http://www.sssxx49.com/htm/index3.html";
    public static String initUrl = "http://www.sssxx49.com/htm/index6.html";
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
                            Toast.makeText(VideoListActivity.this, "到底了", Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        progressDialog.show();

                        postMovies(nextUrl);

//                        Observable.just(nextUrl)
//                                .observeOn(Schedulers.io())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Exception {
//                                        //请求
//                                        getMoviesUrl(s);
//                                    }
//                                });


                    }
                }
            }
        });


        postMovies(initUrl);

    }


    public void postMovies(final String url) {


        if (!isACacheEmpty(url)) {
            Log.i(TAG, "filter  false");
            Log.i(TAG, "test: 缓存中有值，加载缓存");
            String json = getCacheString(url);
            List<Movie> movieList1 = new Gson().fromJson(json, new TypeToken<List<Movie>>() {
            }.getType());
            movieList.addAll(movieList1);
            recyclerView.getAdapter().notifyDataSetChanged();
            return;
        }


        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())

//                .doOnDispose(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.i(TAG, "doOnDispose: ");
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        controlDialog("加载中。。。。。。");

                        Log.i(TAG, "doOnSubscribe");
                    }
                })
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(new Predicate<String>() {
//                    @Override
//                    public boolean test(String url_key) throws Exception {
//                        //此处需要在主线程中运行
//                        if (isACacheEmpty(url_key)) {
//                            Log.i(TAG, "filter  true");
//                            Log.i(TAG, "test: 空的，继续加载");
//                            return true;
//                        } else {
//                            Log.i(TAG, "filter  false");
//                            Log.i(TAG, "test: 缓存中有值，加载缓存");
//                            String json = getCacheString(url_key);
//
//                            List<Movie> movieList1 = new Gson().fromJson(json, new TypeToken<List<Movie>>() {
//                            }.getType());
//
//                            movieList.addAll(movieList1);
//                            recyclerView.getAdapter().notifyDataSetChanged();
//
//
//                            return false;
//                        }
//                    }
//                })
//                .observeOn(Schedulers.io())//切换到线程，进行网络请求
//                .filter(new Predicate<String>() {
//                    @Override
//                    public boolean test(String s) throws Exception {
//                        Log.i(TAG, "filter");
//                        return true;
//                    }
//                })
//                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<List<Movie>>>() {
                    @Override
                    public ObservableSource<List<Movie>> apply(final String requestUrl) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<Movie>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<Movie>> emitter) throws Exception {
                                Log.i(TAG, "map: create  start .....");
                                List<Movie> resultList = getMoviesUrl(requestUrl);
                                if (resultList == null) {
                                    emitter.onError(new Throwable("获取网络失败，请求重试"));
                                } else {
                                    cacheMovieList(requestUrl, resultList);
                                    emitter.onNext(resultList);
                                    emitter.onComplete();
                                }


                                Log.i(TAG, "map: create   end  .....");
                            }
                        });
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {

                                controlDialog("加载重试中。。。。。");
                                Log.i(TAG, "retryWhen: ");
                                return Observable.timer(2000, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                })
//                .map(new Function<String, List<Movie>>() {
//                    @Override
//                    public List<Movie> apply(String requestUrl) throws Exception {
//                        Log.i(TAG, "map: 线程操作");
//                        Log.i(TAG, "apply: map  开始  请求网络。加载数据");
//                        List<Movie> resultList = getMoviesUrl(requestUrl);
//                        cacheMovieList(requestUrl, resultList);
//                        Log.i(TAG, "apply: map  结束  请求网络。传递数据list");
//                        return resultList;
//                    }
//                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "doFinally");
                        Toast.makeText(mActivity, "doFinally", Toast.LENGTH_SHORT).show();
//                        if (progressDialog.isShowing())
//                            progressDialog.dismiss();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies) throws Exception {

                        movieList.addAll(movies);
                        Log.i(TAG, "请求成功");
                        Log.i(TAG, "accept: 请求成功");

                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "请求失败");
                        Log.e(TAG, "accept: 请求失败" + throwable.getMessage());
                        Toast.makeText(mActivity, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        progressDialog.dismiss();
                        Log.i(TAG, "complete ");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                })
        ;
//                .flatMap(new Function<String, ObservableSource<List<Movie>>>() {
//                    @Override
//                    public ObservableSource<List<Movie>> apply(String s) throws Exception {
//
//
//
//
//
//
//                        return null;
//                    }
//                });


//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        //请求
//                        getMoviesUrl(s);
//                    }
//                });
    }


    /*线程中进行缓存*/
    private void cacheMovieList(String urlKey, List<Movie> resultList) {
        Log.i(TAG, "accept: 开始缓存" + urlKey + "  " + resultList.toString());
        ACache.get(mActivity).put(urlKey, new Gson().toJson(resultList));
        Log.i(TAG, "accept: 缓存结束");
    }


    public List<Movie> getMoviesUrl(String mUrl) {
        Log.i(TAG, "开始请求网络。。。。。。。。。: ");

        List<Movie> getMoviesLisst = new ArrayList<>();

        Document document = null;
        try {
            document = Jsoup.connect(mUrl).get();
            Elements elements = document.getElementsByClass("box movie_list").first().getElementsByTag("ul").first().getElementsByTag("li");


            // get next page url

            Elements page_elements =
                    document.getElementsByClass("pagination pagination-lg hidden-xs page");

            Elements elementsa = page_elements.first().select("a");


            for (int i = 0; i < elementsa.size(); i++) {

                if (i == 2) {
                    nextUrl = elementsa.get(i).attr("abs:href");
                }

                Log.i(TAG, "i= " + i + "href: " + elementsa.get(i).attr("abs:href"));

            }


            for (int i = 0; i < elements.size(); i++) {

                Movie movie = new Movie();
                movie.title = elements.get(i).text();
                movie.url = elements.get(i).select("a").attr("abs:href");//获取绝对地址
                movie.headImg = elements.get(i).select("img").attr("src");//获取图片地址


                System.out.println(i + ". " + elements.get(i).text());


                Log.i(TAG, "getMoviesUrl: " + elements.get(i).text());
                Log.i(TAG, "movie: " + movie.toString());

                getMoviesLisst.add(movie);


//                System.out.println(i + ". " + elements.get(i).text());
            }


            for (Element element : elementsa) {
                Log.i(TAG, "href: " + element.attr("abs:href"));
                Log.i(TAG, "text: " + element.text());
            }


            for (Element page_element : page_elements) {
                Log.i(TAG, "page_element: " + page_element.text());

            }

//            Observable.just(movieList)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doFinally(new Action() {
//                        @Override
//                        public void run() throws Exception {
//                            progressDialog.dismiss();
//                        }
//                    })
//                    .subscribe(new Consumer<List<Movie>>() {
//                        @Override
//                        public void accept(List<Movie> movies) throws Exception {
//                            recyclerView.getAdapter().notifyDataSetChanged();
//                        }
//                    });


        } catch (IOException e) {
            Log.i(TAG, "失败了。。。。。。。。。: " + e.getMessage());
            getMoviesLisst = null;

//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//
//            }
            Toast.makeText(mActivity, "链接失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Log.i(TAG, "结束请求网络。。。。。。。。。: ");

        return getMoviesLisst;


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
            holder.title.setText(urlList.get(position).title);
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
                        .placeholder(R.mipmap.bg)
                        .error(R.drawable.jc_error_normal)
                        .into(holder.imageView);
            }

            ((ViewGroup) holder.imageView.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.title = urlList.get(position).title;
                    VideoActivity.initUrl = urlList.get(position).url;
                    mContext.startActivity(new Intent(mContext, VideoActivity.class));
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


    public boolean isACacheEmpty(String key) {

        boolean isnull = "[]".equals(ACache.get(mActivity).getAsString(key));
        Log.i(TAG, "isnull: " + isnull);

        return TextUtils.isEmpty(ACache.get(mActivity).getAsString(key)) || isnull;
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
