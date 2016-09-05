package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Timeout操作符给Observable加上超时时间，每发射一个数据后就重置计时器，当超过预定的时间还没有发射下一个数据，就抛出一个超时的异常。
 Rxjava将Timeout实现为很多不同功能的操作符，比如说超时后用一个备用的Observable继续发射数据等。
 */
public class TimeoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("timeout");
        mLButton.setOnClickListener(e -> timeoutObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                log(e);
            }

            @Override
            public void onNext(Integer integer) {
                log("timeout:" + integer);
            }
        }));
        mRButton.setText("timeoutobserver");
        mRButton.setOnClickListener(e -> timeoutobserverObserver().subscribe(i -> log(i)));
    }

    private Observable<Integer> timeoutObserver() {
        return createObserver().timeout(200, TimeUnit.MILLISECONDS);
    }

    private Observable<Integer> timeoutobserverObserver() {
        return createObserver().timeout(200, TimeUnit.MILLISECONDS, Observable.just(5, 6));
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i <= 3; i++) {
                    try {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }
}
