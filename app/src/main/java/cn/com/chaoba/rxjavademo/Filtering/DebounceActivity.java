package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DebounceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("throttleWithTimeout");
        mLButton.setOnClickListener(e -> throttleWithTimeoutObserver().subscribe(i -> log("throttleWithTimeout:" + i)));
        mRButton.setText("debounce");
        mRButton.setOnClickListener(e -> debounceObserver().subscribe(i -> log("debounce:" + i)));
    }

    /**
     * debounce操作符就是起到了限流的作用，可以理解为阀门，当你半开阀门的时候，水会以较慢的速度流出来
     * 。不同之处就是阀门里的水不会浪费掉，而debounce过滤掉的数据会被丢弃掉
     * 。在Rxjava中，将这个操作符分为了throttleWithTimeout和debounce两个操作符
     * @return
     */
    private Observable<Integer> debounceObserver() {
        //只有那些调用了onCompleted方法的数据才会被发射出来，其他的都过滤掉了。
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).debounce(integer -> {
            log(integer);
            return Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    if (integer % 2 == 0 && !subscriber.isUnsubscribed()) {
                        log("complete:" + integer);
                        subscriber.onCompleted();
                    }
                }
            });
        })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Integer> throttleWithTimeoutObserver() {
        /**
         * √这个操作符通过时间来限流，源Observable每次发射出来一个数据后就会进行计时
         * ，如果在设定好的时间结束前源Observable有新的数据发射出来，这个数据就会被丢弃
         * ，同时重新开始计时。如果每次都是在计时结束前发射数据，那么这个限流就会走向极端：只会发射最后一个数据。
         */
        return createObserver().throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
    }
}