package luocaca.rxretrofit.ui;

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

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.bean.Book;
import luocaca.rxretrofit.ui.viewbigimage.ViewBigImageActivity;
import retrofit2.Retrofit;

/**
 * 加载大类 列表（）。跳转到  图片分类 分类
 */

public class PageJsoupActivity_bak extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PageJsoupActivity";

    private ProgressDialog dialog;

//    public String initUrl = "http://www.dytt8.net/html/gndy/dyzz/index.html";

    @BindView(R.id.rv_simple)
    public RecyclerView recyclerView;

    @BindView(R.id.tv_page_log)
    TextView log;

    @Inject
    Retrofit retrofit;
    private String lastUrl;
    private String nextUrl;
    private ArrayList<String> dataList = new ArrayList<>();


    @Override
    public void initView(Bundle savedInstanceState) {
//        final String initUrl = "http://www.sssxx49.com/html/mode/index1309.html";
        final String initUrl = "http://www.sssxx49.com/html/aky/index44.html";

        dialog = new ProgressDialog(PageJsoupActivity_bak.this);
        dialog.setMessage("加载中...");
        dialog.show();


        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);//定义瀑布流管理器，第一个参数是列数，第二个是方向。
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
        recyclerView.setLayoutManager(layoutManager);//设置瀑布流管理器


        recyclerView.setAdapter(new MyAdapter(dataList, this));



        Observable.just("")
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: ");
                        test1(initUrl);
                    }
                });

    }


    //一：在电影天堂获取一个电影的下载地址
    public void test1(String url) {


        Log.i(TAG, "test1: " + url);

        Document doc;

        try {


            //http://www.sssxx49.com
            //a href=/html/mode/index1310.html>
            String baseUrl = "http://www.sssxx49.com";
//            doc = Jsoup.connect("http://www.dytt8.net/html/gndy/dyzz/index.html").get();
            doc = Jsoup.connect(url).get();


            String title = doc.title();

//            Log.i("title", title);
            printLog("title " + title);

//            Elements srcs = doc.select("a[href]");
            Elements a_tags = doc.select("a[href]");

            //倒数5 下一页
            //倒数6  上一页
            if (a_tags.size() > 6) {
                lastUrl = baseUrl + a_tags.get(a_tags.size() - 6).attr("href");
                nextUrl = baseUrl + a_tags.get(a_tags.size() - 5).attr("href");
                Log.i(TAG, "上一页" + baseUrl + a_tags.get(a_tags.size() - 6));
                Log.i(TAG, "下一页" + baseUrl + a_tags.get(a_tags.size() - 5));
            }


            Elements div = doc.select("div[AttrName=content]");

            for (int i = 0; i < div.size(); i++) {
                String a_href = div.attr("href");
                String src = div.attr("src");
                Log.i(TAG, "<a>:" + a_href);
                Log.i(TAG, "<src>:" + src);
            }

//，doc.select("div[AttrName=abc]"),

//            Set<String> set = new HashSet<>();
//            set.add("content");

//            Element content =  doc.classNames(set);


//            Log.i("content", "test1: "+content.attr("href"));


            Elements srcs = doc.select("img"); //打印所有src 地址
            Element srcStart = srcs.first();
            Log.i("srcs.size()", srcs.size() + "");
            lastIndex = srcs.size();
            printLog(srcs);
            //打印所有 src 地址
            for (int i = 0; i < srcs.size(); i++) {


//                Log.i("", "<a>: " + srcs.get(i));
                Log.i("src", "<a>: " + srcs.attr("src"));

//                printLog("<a>: " + srcs.attr("src"));
//                printLog("<a>: " + srcs.get(i));
            }


        } catch (IOException e) {
            printLog("解析失败" + e.getMessage());
            e.printStackTrace();
        }


    }


    @BindView(R.id.test1)
    TextView viewImgs;

    private void printLog(Elements srcs) {
//        Observable.just(log.getText() + "\n" + string)
        Observable.fromIterable(srcs)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        viewImgs.setText("查看所有图片\n" + dataList.size() + "张图片");
                        recyclerView.getAdapter().notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<Element>() {
                    @Override
                    public void accept(Element e) throws Exception {
                        dataList.add(e.attr("src"));
                        log.setText(log.getText() + "\n" + e.attr("src"));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        log.setText(throwable.getMessage());
                    }
                });
    }


    int lastIndex = -1;

    @Override
    public int BindLayoutID() {
        return R.layout.activity_page_jsout_simple;
    }


    public void printLog(String string) {
        Observable.just(log.getText() + "\n" + string)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        log.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        log.setText(throwable.getMessage());
                    }
                });


    }


    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;

    @OnClick({R.id.button5, R.id.button6, R.id.test1, R.id.tv_page_log})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_page_log:
            case R.id.test1:
                Bundle bundle = new Bundle();
                bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                bundle.putInt("code", dataList.size() - 1 - lastIndex);//第几张
                bundle.putStringArrayList("imageuri", dataList);
                Intent intent = new Intent(PageJsoupActivity_bak.this, ViewBigImageActivity.class);
                intent.putExtras(bundle);
                PageJsoupActivity_bak.this.startActivity(intent);

                break;
            case R.id.button5:

                dialog.setMessage("加载中...");
                dialog.show();

                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                test1(lastUrl);
                            }
                        });
                break;
            case R.id.button6:
                dialog.setMessage("加载中...");
                dialog.show();

                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                test1(nextUrl);
                            }
                        });
                break;
        }
//        button5.setText(lastUrl);
//        button6.setText(nextUrl);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {

            if (!TextUtils.isEmpty(urlList.get(position))) {
                Glide.with(mContext).load(urlList.get(position)).into(holder.imageView);
            }
            Log.i("url", ": " + urlList.get(position));


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
