package luocaca.rxretrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.bean.Book;
import luocaca.rxretrofit.cache.ACache;


public class BookListActivity1 extends BaseActivity {

    private static final String TAG = "BookListActivity1";
    @BindView(R.id.recycle)
    RecyclerView recyclerView;


    public MyAdapter myAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(BookListActivity1.this));

        requestBooks();

    }

    @Override
    public int BindLayoutID() {
        return R.layout.activity_book_list;
    }


    public static String initUrl = "http://www.sssxx49.com/html/aky/index20.html";


    private void requestBooks() {


        if (!TextUtils.isEmpty(ACache.get(mActivity).getAsString(initUrl)) && !ACache.get(mActivity).getAsString(initUrl).equals("[]")) {
            String json = ACache.get(mActivity).getAsString(initUrl);
            Type type = new TypeToken<List<Book>>() {
            }.getType();
            List<Book> list = new Gson().fromJson(json, type);
//            List<Book> list = ACache.get(mActivity).getAsObject(initUrl);
            myAdapter = new MyAdapter(list, BookListActivity1.this);
            recyclerView.setAdapter(myAdapter);
            return;
        }

        dialog.show();


//        api = retrofit.create(Api.class);

//        Observable<ApiResult<List<Book>>> bookList = api.requestBookList();


        Observable.just(initUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                Log.i(TAG, "apply: 失败重新订阅");
                                Log.i(TAG, "retryWhen ");

                                return Observable.timer(2000, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<Book>>>() {
                    @Override
                    public ObservableSource<List<Book>> apply(String url) throws Exception {


                        return Observable.create(new ObservableOnSubscribe<List<Book>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<Book>> e) throws Exception {
                                Log.i(TAG, "flatMap  start: ");


                                Log.i("url", "apply: " + initUrl);

                                List<Book> books = new ArrayList<Book>();
                                //执行网络请求
                                try {
                                    Document document = Jsoup.connect(initUrl).get();

                                    Elements elements = document.getElementsByTag("li");

                                    for (Element element : elements) {
                                        Book book = new Book();
                                        book.setUrl(element.select("a").attr("abs:href"));
                                        book.setName(element.select("a").text());
                                        Log.i("url", ": " + element.select("a").attr("abs:href"));
                                        Log.i("text", ": " + element.text());
                                        books.add(book);
                                    }
                                    e.onNext(books);
                                } catch (Exception ee) {
                                    e.onError(ee);
                                    Toast.makeText(BookListActivity1.this, "" + ee.getMessage(), Toast.LENGTH_SHORT).show();
                                    ee.printStackTrace();
                                }

                                Log.i(TAG, "flatMap  end: ");

                                dialog.dismiss();

                            }
                        });

                    }
                })
//                .map(new Function<List<Book>, List<Book>>() {
//                    @Override
//                    public List<Book> apply(List<Book> list) throws Exception {
//                        return null;
//                    }
//
//                    @Override
//                    public List<Book> apply(String url) throws Exception {
//
//
//
//
//                        return books;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "doFinally: ");
                        dialog.dismiss();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Book>>() {
                    @Override
                    public void accept(List<Book> url) throws Exception {
                        Log.i(TAG, "next: ");

//                        Toast.makeText(BookListActivity.this, "" + bookApiResult.toString(), Toast.LENGTH_SHORT).show();
//                        if (myAdapter == null) {

                        ACache.get(mActivity).put(initUrl, new Gson().toJson(url));

                        myAdapter = new MyAdapter(url, BookListActivity1.this);

                        recyclerView.setAdapter(myAdapter);
//                        }else{
//
//                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "error: ");

                        Toast.makeText(BookListActivity1.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        public List<Book> bookList;
        public Context mContext;


        public MyAdapter(List<Book> list, Context context) {

            if (list != null && list.size() > 27) {
                for (int i = 0; i < 27; i++) {
                    list.remove(0);
                }
            }

            bookList = list;
            mContext = context;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book1, null);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {


            Log.i("url", ": " + bookList.get(position).getUrl());

            holder.tv_title.setText(bookList.get(position).getName());
            holder.tv_url.setText(bookList.get(position).getUrl());


            if (bookList.get(position).getName().equals("下一页") || bookList.get(position).getName().equals("上一页")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initUrl = bookList.get(position).getUrl();
                        requestBooks();
                        Log.i("onClick", bookList.get(position).toString());
                    }
                });
            } else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BookOneDetail.initUrl = bookList.get(position).getUrl();
                        startActivity(new Intent(mContext, BookOneDetail.class));


                        Log.i("onClick", bookList.get(position).toString());
                    }
                });
            }


//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
//                    bundle.putInt("code", position);//第几张
//                    bundle.putStringArrayList("imageuri", getListStr(bookList));
//                    Intent intent = new Intent(mContext, ViewBigImageActivity.class);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }
//            });


        }

        private ArrayList<String> getListStr(List<Book> bookList) {
            ArrayList<String> lis = new ArrayList<>();

            for (Book bk : bookList) {
                lis.add(bk.getUrl());
            }
//            bookList.forEach(book -> {
//                lis.add(book.getUrl());
//            });
            return lis;

        }


        @Override
        public int getItemCount() {
            return bookList.size();
        }
    }


    class MyHolder extends RecyclerView.ViewHolder {


        TextView tv_title;
        TextView tv_url;


        public MyHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_url = itemView.findViewById(R.id.tv_url);

        }
    }


}


