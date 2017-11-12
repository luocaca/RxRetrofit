package luocaca.rxretrofit.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import luocaca.rxretrofit.cache.ACache;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class BookOneDetail extends BaseActivity {
    private static final String TAG = "BookOneDetail";
    @BindView(R.id.tv_book_content)
    TextView tv_book_content;

    public static String initUrl = "";


    @Override
    public void initView(Bundle savedInstanceState) {

        if (!TextUtils.isEmpty(ACache.get(mActivity).getAsString(initUrl))) {

            Log.i(TAG, "initView: 已经加载过内从。从缓存中获取");
//            String result = tranferText(ACache.get(mActivity).getAsString(initUrl));

            tv_book_content.setText(ACache.get(mActivity).getAsString(initUrl));
            return;
        }


        dialog.show();
        Observable.just(initUrl)
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String s) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                String content = "";
                                try {

                                    Document document = Jsoup.connect(s).get();

                                    content = document.getElementsByClass("content").first().text();
                                    e.onNext(content);
                                    e.onComplete();
                                } catch (Exception e1) {
                                    content = e1.getMessage();
                                    e1.printStackTrace();
                                    e.onError(new Throwable(content));
                                }

                            }
                        });
                    }
                })
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(String url) throws Exception {
//                        String content = "";
//                        try {
//
//                            Document document = Jsoup.connect(url).get();
//
//                            content = document.getElementsByClass("content").first().text();
//                        } catch (Exception e) {
//                            content = e.getMessage();
//                            e.printStackTrace();
//                        }
//                        return content;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String content) throws Exception {
                        ACache.get(mActivity).put(initUrl, content, ACache.TIME_DAY * 10);
                        String result = tranferText(content);
                        tv_book_content.setText(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tv_book_content.setText(throwable.getMessage());
                    }
                });


    }


    //对文本进行变换   实现换行
    private String tranferText(String content) {

        String str = "";
        Log.i(TAG, "前: \n"+content);
        str = content.replaceAll("。 　　", "。                     \n" + "        ");
        Log.i(TAG, "后: \n"+str.toString());

        return str.trim();

    }

    @Override
    public int BindLayoutID() {
        return R.layout.book_detail;
    }
}
