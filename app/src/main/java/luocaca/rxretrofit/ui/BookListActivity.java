package luocaca.rxretrofit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.bean.ApiResult;
import luocaca.rxretrofit.bean.Book;
import luocaca.rxretrofit.bean.Person;
import luocaca.rxretrofit.di.component.DaggerBookListComponent;
import luocaca.rxretrofit.di.module.ClientModel;
import luocaca.rxretrofit.http.Api;
import luocaca.rxretrofit.ui.viewbigimage.ViewBigImageActivity;
import retrofit2.Retrofit;


public class BookListActivity extends AppCompatActivity {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    @Inject
    public Person per;

    @Inject
    public Retrofit retrofit;

    private Api api;


    public MyAdapter myAdapter;
    private StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DaggerBookListComponent.builder()
                .clientModel(new ClientModel())
                .build()
                .inject(this);


        setContentView(R.layout.activity_book_list);

        ButterKnife.bind(this);

        Toast.makeText(this, "persion=" + per.toString(), Toast.LENGTH_SHORT).show();




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

//        recyclerView.setLayoutManager(new GridLayoutManager(BookListActivity.this));


        requestBooks();

    }

    private void requestBooks() {

        api = retrofit.create(Api.class);

        Observable<ApiResult<List<Book>>> bookList = api.requestBookList();

        bookList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResult<List<Book>>>() {
                    @Override
                    public void accept(ApiResult<List<Book>> bookApiResult) throws Exception {
//                        Toast.makeText(BookListActivity.this, "" + bookApiResult.toString(), Toast.LENGTH_SHORT).show();

                        if (myAdapter == null) {


                            myAdapter = new MyAdapter(bookApiResult.getData(), BookListActivity.this);

                            recyclerView.setAdapter(myAdapter);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(BookListActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        public List<Book> bookList;
        public Context mContext;


        public MyAdapter(List<Book> list, Context context) {
            bookList = list;
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
//            holder.tv1.setText(bookList.get(position).getName());
//            holder.tv2.setText(bookList.get(position).getNumber() + "");
//            holder.tv3.setText(bookList.get(position).getDetail());
//            holder.imageView.

            if (!TextUtils.isEmpty(bookList.get(position).getUrl())) {
                Glide.with(mContext).load(bookList.get(position).getUrl()).into(holder.imageView);

            }
            Log.i("url", ": " + bookList.get(position).getUrl());


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                    bundle.putInt("code", position);//第几张
                    bundle.putStringArrayList("imageuri", getListStr(bookList));
                    Intent intent = new Intent(mContext, ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
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

        //        public TextView tv1;
//        public TextView tv2;
//        public TextView tv3;
        public ImageView imageView;
//        public Button delete;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}


