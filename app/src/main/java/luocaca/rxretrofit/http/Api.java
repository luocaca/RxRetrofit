package luocaca.rxretrofit.http;

import java.util.List;

import io.reactivex.Observable;
import luocaca.rxretrofit.bean.AllCity;
import luocaca.rxretrofit.bean.ApiResult;
import luocaca.rxretrofit.bean.Book;
import luocaca.rxretrofit.bean.ObjectResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/26 0026.
 */

public interface Api {

//      String BASE_URL_REMOTE = "http://192.168.0.11/videoworld/v1/"; //远程服务器的

    String BASE_URL_REMOTE = "http://115.159.196.175/VideoWorld/v1/"; //远程服务器的

//    String BASE_URL = "http://v.juhe.cn/weather/";


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

    @GET
    Observable<ObjectResponse> requestGetMovies(@Url String baseUrl, @Query("url") String url);

    @GET
    Observable<ObjectResponse> requestAddMovies(@Url String baseUrl, @Query("url") String url, @Query("list") String list);


}
