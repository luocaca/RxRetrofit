package luocaca.rxretrofit.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import luocaca.rxretrofit.cache.ACache;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    protected Activity mActivity;


    public ProgressDialog dialog;

    protected ACache mACache;


    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mACache = ACache.get(mActivity);
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("加载中。。。。。");
        dialog.setCanceledOnTouchOutside(false);


        if (BindLayoutID() != 0) {
            //当 集成baseActivity 的 act 不设定 id 。默认为0
            setContentView(BindLayoutID());
            mUnbinder = ButterKnife.bind(this);
        }

        initView(savedInstanceState);

    }

    public abstract void initView(Bundle savedInstanceState);


    public abstract int BindLayoutID();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();

        this.mUnbinder = null;
    }
}
