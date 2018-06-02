package com.example.goals.hooktest;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Date: 2018/6/2.
 * Description:
 *
 * @author huyongqiang
 */

public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";
    public static final String TARGET_INTENT_NAME = "com.example.goals.hooktest.TargetActivity";


    //==============================方法一===============================

    /**
     * 步骤一：写的是启动TargetActivity，但是真正启动的是StubActivity。说明中间被我们替换了
     *
     * @throws Exception
     */
    public static void hookAMS() throws Exception {
        Object defaultSingleton = null;
        if (Build.VERSION.SDK_INT >= 26) {//1
            Class<?> activityManageClazz = Class.forName("android.app.ActivityManager");
            //获取activityManager中的IActivityManagerSingleton字段
            defaultSingleton = FieldUtil.getField(activityManageClazz, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            //获取ActivityManagerNative中的gDefault字段
            defaultSingleton = FieldUtil.getField(activityManagerNativeClazz, null, "gDefault");
        }
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField = FieldUtil.getField(singletonClazz, "mInstance");//2
        //获取iActivityManager
        Object iActivityManager = mInstanceField.get(defaultSingleton);//3
        Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iActivityManagerClazz}, new IActivityManagerProxy(iActivityManager));
        mInstanceField.set(defaultSingleton, proxy);
    }

    /**
     * 步骤二：实现启动未注册的TargetActivity
     *
     * @throws Exception
     */
    public static void hookHandler() throws Exception {
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = FieldUtil.getField(activityThreadClass, null, "sCurrentActivityThread");//1
        //Field mHField = FieldUtil.getField(activityThread,"mH");//2
        Field mHField = FieldUtil.getField(activityThreadClass, "mH");//2
        Handler mH = (Handler) mHField.get(currentActivityThread);//3
        FieldUtil.setField(Handler.class, mH, "mCallback", new HCallback(mH));
    }

    //==============================方法二===============================

    /**
     * 实现的是没有在Manifest中注册的TargetActivity也可以被启动
     *
     * @param context
     * @throws Exception
     */
    public static void hookInstrumentation(Context context) throws Exception {
        Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
        Field mMainThreadField = FieldUtil.getField(contextImplClass, "mMainThread");//1
        Object activityThread = mMainThreadField.get(context);//2
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field mInstrumentationField = FieldUtil.getField(activityThreadClass, "mInstrumentation");//3
        FieldUtil.setField(activityThreadClass, activityThread, "mInstrumentation", new InstrumentationProxy((Instrumentation)
                mInstrumentationField.get(activityThread),
                context.getPackageManager()));
    }
}
