package com.example.goals.hooktest;

import android.app.Application;
import android.content.Context;

/**
 * Date: 2018/6/2.
 * Description:
 *
 * @author huyongqiang
 */

public class App extends Application {
    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            //方法一：
            //步骤一：写的是启动TargetActivity，但是真正启动的是StubActivity。说明中间被我们替换了
//            HookHelper.hookAMS();
            //步骤二：实现启动未注册的TargetActivity
//            HookHelper.hookHandler();

            //方法二：
            //实现的是没有在Manifest中注册的TargetActivity也可以被启动
            HookHelper.hookInstrumentation(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
