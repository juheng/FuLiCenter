package cn.ucai.fulicenter.application;

import android.app.Application;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/1/10 0010.
 */
public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication instance;
    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static User user;

    public static void setInstance(FuLiCenterApplication instance) {
        FuLiCenterApplication.instance = instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
