package luocaca.rxretrofit.ui.maomi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.bean.Book;
import luocaca.rxretrofit.bean.JsoupPageBean;
import luocaca.rxretrofit.cache.ACache;
import luocaca.rxretrofit.http.JsoupUtil;
import luocaca.rxretrofit.ui.viewbigimage.ViewBigImageActivity;

/**
 * 猫咪看图
 */

public class MaoMiiImagesActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PageJsoupActivity";

    private ProgressDialog dialog;

//    public String initUrl = "http://www.dytt8.net/html/gndy/dyzz/index.html";

//    @BindView(R.id.tv_page_log)
//    TextView log;


    @BindView(R.id.rv_simple)
    public RecyclerView recyclerView;

    private String lastUrl;
    private String nextUrl;
    private ArrayList<String> dataList = new ArrayList<>();
    public static String initUrl = "https://www.661cf.com/htm/piclist1";

    public JsoupPageBean pageBean = new JsoupPageBean();
    private StaggeredGridLayoutManager layoutManager;


    @Override
    public void initView(Bundle savedInstanceState) {

        lastUrl = initUrl;
        pageBean.currentUrl = initUrl;

        aCache = ACache.get(mActivity);
//        initUrl = "http://www.sssxx49.com/html/mode/index1309.html";

        dialog = new ProgressDialog(MaoMiiImagesActivity.this);
        dialog.setMessage("加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //定义瀑布流管理器，第一个参数是列数，第二个是方向。
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        recyclerView.setLayoutManager(layoutManager);//设置瀑布流管理器

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        recyclerView.setAdapter(new MyAdapter(dataList, this));

        getResByJsoup(pageBean.currentUrl);

    }


    //一：在电影天堂获取一个电影的下载地址
    public void test1(String url) {
        pageBean.imageList.clear();

        pageBean.currentUrl = url;
        Log.i(TAG, "test1: " + url);
        Document doc;
        try {
            //http://www.sssxx49.com
            //a href=/html/mode/index1310.html>
            String baseUrl = "";
//            String baseUrl = "http://www.sssxx49.com";
//            doc = Jsoup.connect("http://www.dytt8.net/html/gndy/dyzz/index.html").get();
//            doc = Jsoup.connect(url).get();
            doc = JsoupUtil.getMaoMiHtmlPage(url);

            String title = doc.title();
            pageBean.title = title;
//            Log.i("title", title);
            printLog("title " + title);

//            Elements srcs = doc.select("a[href]");
            Elements a_tags = doc.select("a[href]");

            //倒数5 下一页
            //倒数6  上一页

            Element xia = doc.getElementsContainingOwnText("下一篇").first();
            Element xia1 = doc.getElementsContainingText("下一篇").first();
//                lastUrl = baseUrl + a_tags.get(a_tags.size()).attr("href");
//                nextUrl = baseUrl + a_tags.get(a_tags.size() - 10).attr("href");
            //	<ul><span class="last">上一篇:</span> <span class="next">下一篇:下一篇：
            // <a href='/htm/girl16/2381.htm'>第133期</a></span></ul>
            if (xia != null) {
                pageBean.nextUrl = xia.getElementsByTag("a").first().attr("abs:href");
                pageBean.lastUrl = lastUrl;

            }


            Elements srcs = doc.select("img"); //打印所有src 地址
            Element srcStart = srcs.first();
            Log.i("srcs.size()", srcs.size() + "");
            lastIndex = srcs.size();

            //打印所有 src 地址
            for (int i = 0; i < srcs.size(); i++) {
                Log.i("src", "<a>: " + srcs.get(i).attr("src"));
                pageBean.imageList.add(srcs.get(i).attr("src"));
            }

            printLog(pageBean);

        } catch (IOException e) {
            printLog("解析失败" + e.getMessage());
            e.printStackTrace();
        }


    }


    @BindView(R.id.test1)
    TextView viewImgs;

    private void printLog(final JsoupPageBean jsoupPageBean) {
        pageBean = jsoupPageBean;
        aCache.put(jsoupPageBean.currentUrl, jsoupPageBean, ACache.TIME_DAY * 10);

//        Observable.just(log.getText() + "\n" + string)
        Observable.just(jsoupPageBean)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dataList.addAll(jsoupPageBean.imageList);
                        viewImgs.setText("查看所有图片\n" + dataList.size() + "张图片");
                        recyclerView.getAdapter().notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).subscribe();


    }


    int lastIndex = -1;

    @Override
    public int BindLayoutID() {
        return R.layout.activity_page_jsout_simple;
    }


    public void printLog(String string) {
        Observable.just(string)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(mActivity, "" + s, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mActivity, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;

    @OnClick({R.id.button5, R.id.button6, R.id.test1})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test1:
                Bundle bundle = new Bundle();
                bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                bundle.putInt("code", dataList.size() - 1 - lastIndex);//第几张
                bundle.putStringArrayList("imageuri", dataList);
                Intent intent = new Intent(MaoMiiImagesActivity.this, ViewBigImageActivity.class);
                intent.putExtras(bundle);
                MaoMiiImagesActivity.this.startActivity(intent);

                break;
            case R.id.button5:


                if (TextUtils.isEmpty(pageBean.lastUrl) || pageBean.lastUrl.contains("/html/aky")) {
                    Toast.makeText(this, "没有上一页", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.setMessage("加载中...");
                dialog.show();
                Log.i(TAG, "lastUrl: " + lastUrl);
                getResByJsoup(pageBean.lastUrl);
//                Observable.just("")
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                test1(lastUrl);
//                            }
//                        });
                break;
            case R.id.button6:

                if (TextUtils.isEmpty(pageBean.nextUrl) || pageBean.nextUrl.contains("/html/aky")) {
                    Toast.makeText(this, "没有下一页了", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.setMessage("加载中...");
                dialog.show();
                Log.i(TAG, "nextUrl: " + pageBean.nextUrl);

                getResByJsoup(pageBean.nextUrl);

//                Observable.just("")
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                test1(nextUrl);
//                            }
//                        });
                break;
        }
//        button5.setText(lastUrl);
//        button6.setText(nextUrl);
    }


    Gson gson = new Gson();
    ACache aCache;

    public void getResByJsoup(String url) {
        Observable.just(url)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String url_key) throws Exception {

                        if (aCache.getAsObject(url_key) == null) {
                            Log.i(TAG, url_key + "本页地址没有缓存过，。。。。进行网络请求");
                            return true;
                        } else {
                            JsoupPageBean jsonPageBean = (JsoupPageBean) aCache.getAsObject(url_key);
                            Log.i(TAG, url_key + "本页地址已经缓存过。直接获取缓存数据，。。。。");
                            //jsonCache
                            //json 解析
//                            TypeToken typeToken = ,new TypeToken<List<String>>(){};
//
                            printLog(jsonPageBean);

//                            List<String> ps = gson.fromJson(jsonCache, new TypeToken<List<String>>() {
//                            }.getType());
//                            printLog(ps);
                            return false;
                        }
                    }
                })
                .observeOn(Schedulers.io())//切换回io线程，进行界面请求
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String url) throws Exception {
                        test1(url);
                    }
                });
    }


    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        public List<String> urlList;
        public Context mContext;


        public MyAdapter(List<String> list, Context context) {
            urlList = list;
            mContext = context;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_images, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {

            if (!TextUtils.isEmpty(urlList.get(position))) {

                Glide.with(mContext)
                        .load(urlList.get(position))
                        .crossFade(700)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .centerCrop().override(1080, 1080*3/4)
                        .into(holder.imageView);
            }

            Log.i(TAG + "url", ": " + urlList.get(position));


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                    bundle.putInt("code", position);//第几张
                    bundle.putStringArrayList("imageuri", dataList);
                    Intent intent = new Intent(mContext, ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });


        }

        int count = 0;

        private ArrayList<String> getListStr(List<Book> urlList) {
            ArrayList<String> lis = new ArrayList<>();

            for (Book bk : urlList) {
                lis.add(bk.getUrl());
            }
//            urlList.forEach(book -> {
//                lis.add(book.getUrl());
//            });
            return lis;

        }


        @Override
        public int getItemCount() {
            return urlList.size();
        }
    }


    class MyHolder extends RecyclerView.ViewHolder {


        public ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }


}
