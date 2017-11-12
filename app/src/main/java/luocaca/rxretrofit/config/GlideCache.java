package luocaca.rxretrofit.config;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class GlideCache implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        File storageDirectory = Environment.getExternalStorageDirectory();
        String downloadDirectoryPath = storageDirectory + "/GlideCache";
        Log.e("GlideCache", "applyOptions: " +downloadDirectoryPath);
        //设置缓存的大小为100M
        int cacheSize = 100 * 1000 * 1000 * 20;
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));

        // 设置磁盘高速缓存的位置、大小
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, downloadDirectoryPath, cacheSize));


        // 设置磁盘高速缓存的大小 100MB
//        builder.setDiskCache( new InternalCacheDiskCacheFactory(context, 104857600));

        Log.e("GlideCache", "applyOptions: 配置glide");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
