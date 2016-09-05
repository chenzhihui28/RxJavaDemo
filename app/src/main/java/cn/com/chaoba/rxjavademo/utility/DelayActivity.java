package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 顾名思义，Delay操作符就是让发射数据的时机延后一段时间，这样所有的数据都会依次延后一段时间发射
 * 。在Rxjava中将其实现为Delay和DelaySubscription。不同之处在于Delay是延时数据的发射，而DelaySubscription是延时注册Subscriber。
 */
public class DelayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("delay");
        mLButton.setOnClickListener(e -> {
            log("start subscrib:" + getCurrentTime());
            delayObserver().subscribe(i -> log("delay:" + (getCurrentTime() - i)));
        });
        mRButton.setText("delaySubscription");
        mRButton.setOnClickListener(e -> {
            log("start subscrib:" + getCurrentTime());
            delaySubscriptionObserver().subscribe(i -> log("delaySubscription:" + i));
        });
    }

    private Observable<Long> delayObserver() {
        return createObserver(2).delay(2000, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> delaySubscriptionObserver() {
        return createObserver(2).delaySubscription(2000, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> createObserver(int index) {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                log("subscrib:" + getCurrentTime());
                for (int i = 1; i <= index; i++) {
                    subscriber.onNext(getCurrentTime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    private long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }

}
