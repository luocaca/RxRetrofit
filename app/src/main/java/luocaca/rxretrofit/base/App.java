package luocaca.rxretrofit.base;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class App extends Application {
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();




    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return  new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
//                .diskUsage(new MyCoolDiskUsageStrategy())
//                .fileNameGenerator(new VideoActivity.MyFileNameGenerator())
                .maxCacheFilesCount(120)
                .build();
    }
}
