package luocaca.rxretrofit.ui;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import luocaca.rxretrofit.R;
import luocaca.rxretrofit.base.App;
import luocaca.rxretrofit.base.BaseActivity;
import luocaca.rxretrofit.cache.ACache;

/**
 * 用来播放视频  的界面
 */

public class ShowVideoActivity extends BaseActivity {

    private static final String TAG = "VideoActivity";

    public static String title = "大傻么么哒";

    public static String initUrl = "http://www.sssxx49.com/modo/index1932.html";


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @BindView(R.id.open)
    Button open;

    @BindView(R.id.url)
    EditText url;

    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;

    JCVideoPlayerStandard jcVideoPlayerStandard;
    private String staticUrl;


    @Override
    public void initView(Bundle savedInstanceState) {

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "" + url.getText(), Toast.LENGTH_SHORT).show();
                openVideo(url.getText().toString());
            }
        });


        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("正在获取视频地址地址");
        dialog.show();
        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        //SCREEN_LAYOUT_NORMAL
        jcVideoPlayerStandard.thumbImageView.setBackgroundResource(R.drawable.jc_bottom_bg);


//        staticUrl = "http://www.sssxx49.com/play/2043-0-0.html";
        staticUrl = "http://ugcbsy.qq.com/flv/153/109/c01350046ds.p702.1.mp4?sdtfrom=v1010&guid=fa465656cc6dba47f514d75a833e2b5c&vkey=8B3D3C957F42F62B223A92FFD0EE8F4FB8CEA5EE3C52A4E8EE4E67F784B2EE10F4DB1AF0591FF205D6A002D66A1C07266C4C931AA2F9DD4865017264DCA4ABEDFD7C34003D725D7FD4AE34A3257CD0422049B3422EA62ED5505105B4E5D7F25BC3D468EFCF63817B38D0FBCC48B7A421997A4EE38AB27536";


    }


    ProgressDialog dialog;

    public void openVideo(String str) {

        Log.i(TAG, "获取播放地址，进行缓存 ，，，，播放地址对应播放界面地址");
        Log.i(TAG, initUrl + "-->" + str);

        ACache.get(mActivity).put(initUrl, str);

        Log.i(TAG, "openVideo: " + str);

        dialog.dismiss();
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(str);
        Log.i(TAG, "openVideo:........ " + str);
        Log.i(TAG, "proxyUrl:........ " + proxyUrl);
        jcVideoPlayerStandard.setUp(proxyUrl
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, title);
//        sensorPortrait
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "大傻么么哒");
        jcVideoPlayerStandard.startVideo();
        //setImage();
        //"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"

    }

    private HttpProxyCacheServer getProxy() {
        // should return single instance of HttpProxyCacheServer shared for whole app.
        return App.getProxy(mActivity);

    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public int BindLayoutID() {
        return R.layout.activity_video_show;
    }


    public static class MyFileNameGenerator implements FileNameGenerator {

        // Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
        // e. g. http://example.com?videoId=abcqaz&sessionToken=xyz987
        public String generate(String url) {
            Uri uri = Uri.parse(url);
            String videoId = uri.getQueryParameter("videoId");
            return videoId + ".mp4";
        }
    }


}
