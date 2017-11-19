package luocaca.rxretrofit.di.module;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供retrofit 对象
 */

@Module
public class ClientModel {

    //http://www.luocaca.cn/hello/book/allbook
    //http://www.luocaca.cn/hello-ssm/book/allbook

    //    private final String BASE_URL = "http://www.luocaca.cn/hello-ssm/";
    private final String BASE_URL = "http://115.159.196.175/hello-ssm/"; //远程服务器的
//    private final String BASE_URL = "http://115.159.196.175/hello-ssm/";  // 本地主机的

    //    private final static String BASE_URL = "http://192.168.43.103:8089/";
    private static final int TIME_OUT = 10;

    @Singleton
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder.baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //
    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder) {

        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        ;
        return builder.build();
    }

    //
//
    //提供
    @Provides
    //单例
    @Singleton
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    //
//
    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }


}
