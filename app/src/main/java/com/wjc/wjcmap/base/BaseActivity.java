package com.wjc.wjcmap.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.wjc.wjcmap.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * 绑定布局
     */
    protected abstract int bindLayout();

    /**
     * 初始化布局
     */

    protected abstract void initView(View mContextView, @Nullable Bundle savedInstanceState);

    /**
     * 处理业务逻辑
     */

    protected abstract void doBusiness(Context context);


    /**
     * 初始化监听器
     */

    protected void initListener() {

    }
    /**
     * 初始化数据
     */

    protected void initParams() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*加载布局*/
        View mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mContextView);
        setStatuBar();
        initView(mContextView,savedInstanceState);
        initParams();
        initListener();
        doBusiness(this);
    }
        // 设置状态栏颜色
    private void setStatuBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarColor(R.color.color_ffffff,0).statusBarDarkFont(true).init();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration con = new Configuration();
            con.setToDefaults();
            res.updateConfiguration(con, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 页面跳转
     * @param c 目标页面
     * @param isFinish  是否关闭
     */
    public void startActivity(Class c,boolean isFinish){
        startActivity(new Intent(this,c));
        if (isFinish) {
            finish();
        }
    }
    /**
     * 页面跳转
     * @param c 目标页面
     * @param isFinish  是否关闭
     */
    public void startActivity(Class c,Bundle b,boolean isFinish){
        startActivity(new Intent(this,c).putExtras(b));
        if (isFinish) {
            finish();
        }
    }
    public void startActivityForResult(Class c,int requestCode){
        startActivityForResult(new Intent(this,c),requestCode);
    }
}
