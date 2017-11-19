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

import org.jsoup.Jsoup;
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

/**
 * 视频 列表 预览 界面
 */

public class TTLTypeListActivity extends BaseActivity {

    /**
     * <div class="Main1">
     * <div class="m_T1">巨乳视频 <span> <a href="#" target="_blank">更多>></a></span>
     * <div class="FR">
     * <p>
     * 　		</div>
     * </div>
     * <ul class="m_Box1">
     * <p>
     * <li>
     * <p>
     * <p>
     * <div class="con">
     * <a href="/view/index33413.html"
     * class="pic" target="_blank">
     * <p>
     * <img src="http://caopic.8888ruru.com:88/pic/uploadimg/2017-11/201711315592067588.jpg"
     * alt=" [videolist:name len=10]" height="120" width="160" />
     * </a>
     * <p>
     * <a href="/view/index33413.html"
     * class="txt"
     * target="_blank">11-中年大叔真實..</a>
     * <p>
     * <p><span><font color="#FFFFFF" >0</font></span><img src="/template/1/images/pic_2.png" /></p>
     * <p style="margin-top:2px;padding-left:3px;">2017-11-03</p>
     * </div>
     * <p>
     * <p>
     * <p>
     * </li>
     * <p>
     * <li>
     * <div class="con">
     * <a href="/view/index33412.html" class="pic" target="_blank"><img src="http://caopic.8888ruru.com:88/pic/uploadimg/2017-11/201711315585390182.jpg" alt=" [videolist:name len=10]" height="120" width="160" /></a>
     * <a href="/view/index33412.html" class="txt" target="_blank">10-中年大叔真實..</a>
     * <p><span><font color="#FFFFFF" >0</font></span><img src="/template/1/images/pic_2.png" /></p>
     * <p style="margin-top:2px;padding-left:3px;">2017-11-03</p>
     * </div>
     * </li>
     */


    public static String initUrl = "";
    //http://www.2013sss.com/list/index34.html
    private static final String TAG = "TTLActivity";

    ProgressDialog progressDialog;

    @BindView(R.id.recycle_video_list)
    RecyclerView recyclerView;

    //    public String initUrl = "http://www.sssxx49.com/htm/index3.html";
//    public static String initUrl = "http://www.sssxx49.com/htm/index6.html";
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
                            Toast.makeText(TTLTypeListActivity.this, "到底了", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        request(nextUrl);

                    }
                }
            }
        });

        request(initUrl);

    }

    /**
     * <div class="con">
     * <p>
     * <a href="/view/index33413.html"  class="pic"  target="_blank">
     * <img src="http://caopic.8888ruru.com:88/pic/uploadimg/2017-11/201711315592067588.jpg"
     * alt=" [videolist:name len=10]" height="120" width="160" />
     * </a>
     * <p>
     * <a href="/view/index33413.html"   class="txt"  target="_blank">11-中年大叔真實..</a>
     * <p>
     * <p><span><font color="#FFFFFF" >0</font></span><img src="/template/1/images/pic_2.png" /></p>
     * <p style="margin-top:2px;padding-left:3px;">2017-11-03</p>
     * </div>
     * </div>
     *
     * @param initUrl
     */


    private void request(final String initUrl) {


        if (getMoviesFromCache(initUrl) != null) {
            movieList.addAll(getMoviesFromCache(initUrl).movies);
            nextUrl = getMoviesFromCache(initUrl).nextUrl;
            recyclerView.getAdapter().notifyDataSetChanged();
            return;
        }
//        String url = "http://www.2013sss.com/";
        dialog.show();

        Observable.just(initUrl)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<ArrayList<Movie>>>() {
                    @Override
                    public ObservableSource<ArrayList<Movie>> apply(String mUrl) throws Exception {
                        ArrayList<Movie> arrayList = new ArrayList();
                        Document page = Jsoup.connect(mUrl).get();
//                        Elements lis = page.getElementsByClass("n_Box").first().getElementsByTag("li");
                        Elements divs = page.getElementsByClass("con");
                        for (int i = 0; i < divs.size(); i++) {
                            Movie movie = new Movie();
                            //txt
//                            String absHref = link.attr("abs:href"); // "http://www.open-open.com/"
                            String headImg = divs.get(i).select("img").first().attr("src");
                            String href = divs.get(i).select("a").get(1).attr("abs:href");
                            String text = divs.get(i).select("a").get(1).text();
                            //:containsOwn(text):   <a href='/list/index54_2.html'>下一页</a>
                            String mNextUrl = page.getElementsContainingOwnText("下一页").first().attr("abs:href");
//                          String nextUrl = divs.get(i).select("a").get(1).text();

                            /**
                             *
                             <div class="dede_pages">
                             <ul class="pagelist"><span>共534条数据 页次:1/27页</span><em class='nolink'>首页</em>
                             <em class='nolink'>上一页</em>
                             <em>1</em><a href='/list/index54_2.html'>2</a><a href='/list/index54_3.html'>3</a><a href='/list/index54_4.html'>4</a><a href='/list/index54_5.html'>5</a><a href='/list/index54_6.html'>6</a><a href='/list/index54_7.html'>7</a><a href='/list/index54_8.html'>8
                             </a>
                             <a href='/list/index54_2.html'>下一页</a>
                             <a href='/list/index54_27.html'>尾页</a><span><input type='input' name='page' size='4'/><input type='button' value='跳转' onclick="getPageGoUrl(27,'page','/list/index54_<page>.html')" class='btn' /></span></ul>
                             </DIV>

                             */
                            //lastUrl
                            //nextUrl


                            movie.headImg = headImg;
                            movie.url = href;
                            movie.title = text;
                            movie.nextUrl = mNextUrl;
                            nextUrl = mNextUrl;
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
                        addDiskCache(initUrl, movies);
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
//                        movieList.clear();
                        movieList.addAll(value);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TTLTypeListActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onNext: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");

                    }
                })
        ;


    }

    private CacheBean getMoviesFromCache(String cacheKey) {
        String json = mACache.getAsString(cacheKey);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            CacheBean cacheBean = new Gson().fromJson(json, CacheBean.class);
//            ArrayList<Movie> movies = new Gson().fromJson(json, new TypeToken<ArrayList<Movie>>() {
//            }.getType());
            return cacheBean;
        }

    }

    private void addDiskCache(String initUrl, ArrayList<Movie> movies) {
        CacheBean cacheBean = new CacheBean();
        if (movies != null && movies.size() > 0) {
            cacheBean.movies = movies;
            cacheBean.nextUrl = nextUrl;
            mACache.put(initUrl, new Gson().toJson(cacheBean));
            Log.i(TAG, "addDiskCache: 添加硬盘缓存成功，" + movies.toString());
        } else {
            Log.i(TAG, "addDiskCache: 失败，值为空");
        }

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
                        .placeholder(R.mipmap.bg)
                        .error(R.drawable.jc_error_normal)
                        .into(holder.imageView);
            }

            ((ViewGroup) holder.imageView.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TTLVideoActivity.title = urlList.get(position).title;
                    TTLVideoActivity.initUrl = urlList.get(position).url;
                    mContext.startActivity(new Intent(mContext, TTLVideoActivity.class));
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


    class CacheBean {

        public ArrayList<Movie> movies;
        public String nextUrl = "";

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
