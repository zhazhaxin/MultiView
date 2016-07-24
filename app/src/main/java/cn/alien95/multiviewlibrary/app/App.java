package cn.alien95.multiviewlibrary.app;

import android.app.Application;

import cn.alien95.multiviewlibrary.BuildConfig;
import cn.alien95.resthttp.request.RestHttp;

/**
 * Created by linlongxin on 2016/1/27.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RestHttp.initialize(this);
        if(BuildConfig.DEBUG){
            RestHttp.setDebug(true,"network");
        }
    }
}
