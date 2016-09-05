package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Using操作符创建一个在Observable生命周期内存活的资源，也可以这样理解：我们创建一个资源并使用它，用一个Observable来限制这个资源的使用时间
 * ，当这个Observable终止的时候，这个资源就会被销毁。
 Using需要使用三个参数，分别是：
 创建这个一次性资源的函数
 创建Observable的函数
 释放资源的函数
 */
public class UsingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<Long> observable = usingObserver();
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                log("onError");
            }

            @Override
            public void onNext(Object o) {
                log("onNext"+o);
            }
        };
        mLButton.setText("using");
        mLButton.setOnClickListener(e -> observable.subscribe(subscriber));
        mRButton.setText("unSubscrib");
        mRButton.setOnClickListener(e -> subscriber.unsubscribe());
    }

    private Observable<Long> usingObserver() {
        return Observable.using(() -> new Animal(), i -> Observable.timer(5000,TimeUnit.MILLISECONDS), o -> o.relase());
    }

    private class Animal {
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                log("animal eat");
            }
        };

        public Animal() {
            log("create animal");
            Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .subscribe(subscriber);
        }

        public void relase() {
            log("animal released");
            subscriber.unsubscribe();
        }
    }
}
