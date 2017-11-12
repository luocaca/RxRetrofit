package luocaca.rxretrofit.http;

import java.util.List;

import io.reactivex.Observable;
import luocaca.rxretrofit.bean.AllCity;
import luocaca.rxretrofit.bean.ApiResult;
import luocaca.rxretrofit.bean.Book;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public interface Api {

    // BASE_URL = "http://v.juhe.cn/weather/";

    @GET("citys")
    Observable<AllCity> getAllCity(@Query("key") String key);


    /**
     * http://localhost:8089/hello/book/add?name=大傻1&book_id=222&number=222&detail=详细信息
     *
     * @param book_id
     * @param number
     * @param detail
     * @param name
     * @return
     */

    @GET("book/add")
    Observable<ApiResult> requestAdd(@Query("book_id") String book_id,
                                     @Query("number") String number,
                                     @Query("detail") String detail,
                                     @Query("name") String name,
                                     @Query("url") String url
    );


    @GET("book/allbook")
    Observable<ApiResult<List<Book>>> requestBookList();

    @GET("book/del/{bookId}")
    Observable<ApiResult> requestDelete(@Path("bookId") String bookId);


}
