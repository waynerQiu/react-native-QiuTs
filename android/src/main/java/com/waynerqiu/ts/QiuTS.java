package com.waynerqiu.ts;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.util.Log;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/27.
 */
public class QiuTS extends ReactContextBaseJavaModule {
    protected static final String TAG = QiuTS.class.getSimpleName();
    private static final  String TestEventName = "TestEventName";

    private Timer timer;
    public QiuTS(final ReactApplicationContext reactContext) {
        super(reactContext);

        /**
         * 给JS发送事件
         * 原生模块可以在没有被调用的情况下往JS发送事件通知，最简单的办法是通过RCTDeviceEventEmitter，这可以通过ReactContext获得对应的引用
         * 在这里，我们为了能够接收到事件，开启了一个定时器，每一秒发送一次事件
         * 在JS中，我们这里直接使用DeviceEventEmitter模块来监听事件。
         *
         * DeviceEventEmitter.addListener(BGNativeModuleExample.TestEventName, info => {
         *      console.log(info);
         * });
         */
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //发送事件
                WritableMap params = Arguments.createMap();
                params.putString("name","waynerQiu");
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TestEventName,params);
            }
        };
        timer = new Timer();
        timer.schedule(task,1000,1000);;
    }

    /**
     * 定义模块名称,
     * @return 返回一个字符串名字，在JS中我们就使用这个名字调用这个模块
     */
    @Override
    public String getName() {
        return "QiuTS";
    }

    /**
     * 为原生模块添加方法
     * js中调用: QiuTS.testPrint("Jack", {height: '1.78m', weight: '7kg'});
     * 函数参数类型对应: Boolean -> Bool,     Integer -> Number,     Double -> Number,     Float -> Number,     String -> String,     Callback -> function,     ReadableMap -> Object,     ReadableArray -> Array
     *
     * @param name
     * @param info
     */
    @ReactMethod
    public void testPrint(String name,ReadableMap info){
        Log.i(TAG,name);
        Log.i(TAG,info.toString());
    }

    /**
     * 自定义方法，调用原生的Toast
     * @param msg
     * @param duration
     */
    @ReactMethod
    public void show(String msg, int duration) {
        Toast.makeText(getReactApplicationContext(), msg, duration).show();
    }


    /**
     * 回调函数
     * 原生模块还支持一种特殊的参数——回调函数。它提供了一个函数来把返回值传回给JS。
     * js中的调用方法
     * QiuTs..getNativeClass(name => {
     *  console.log("nativeClass: ", name);
     * });
     * @param callback
     */
    @ReactMethod
    public void getNativeClass(Callback callback) {
        callback.invoke("Hello Callback by JAVA");
    }


    /**
     * Promises
     *  原生模块还可以使用promise来简化代码，搭配ES2016(ES7)标准的async/await语法则效果更佳。
     *  如果桥接原生方法的最后一个参数是一个Promise，则对应的JS方法就会返回一个Promise对象。
     * JS中如下调用:
     * QiuTs.testPromises(true).then(result => {
     *      console.log("result is ", result);
     * }).catch(result => {
     *      console.log("result = ", result);
     * });
     *
     * @param isResolve
     * @param promise
     */
    @ReactMethod
    public void testPromises(Boolean isResolve, Promise promise) {
        if(isResolve) {
            promise.resolve(isResolve.toString());
        } else {
            promise.reject(isResolve.toString());
        }
    }


    /**
     * 可以设置一些常量，能够在js层调用，本例中在JS代码中调用如"MyToastAndroid.LONG"
     * @return
     */
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>(2);
        constants.put("TAG", TAG);
        constants.put(TestEventName, TestEventName);
        return constants;
    }


}
