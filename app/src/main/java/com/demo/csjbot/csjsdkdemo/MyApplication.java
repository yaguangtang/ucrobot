package com.demo.csjbot.csjsdkdemo;

import android.app.Application;
import android.util.Log;

import com.csjbot.coshandler.core.CsjRobot;
import com.csjbot.coshandler.listener.OnAuthenticationListener;

/**
 * Created by Administrator on 2019/7/13.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CsjRobot.authentication(this,"eaa67d5e-c120-4cc7-9af2-4197920cbc34", "166285BF869F1D7E77A38B53E2F52F56", new OnAuthenticationListener() {
            @Override
            public void success() {
                Log.d("TAG","授权成功!");
            }

            @Override
            public void error() {
                Log.d("TAG","授权失败!");
            }
        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**
         * 初始化SDK
         */
        CsjRobot.getInstance().init(this);
    }
}
