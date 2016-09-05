package cn.com.chaoba.rxjavademo.errorhandling;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Retry操作符在发生错误的时候回重新进行订阅,而且可以重复多次,所以发射的数据可能产生重复,如果
 * 重复制定次数还有错误的话就会将错误返回给观察者
 * Rxjava还实现了RetryWhen操作符。当错误发生时，retryWhen会接收onError的throwable作为参数，并根据定义好的函数返回一个Observable，如果这个Observable发射一个数据，就会重新订阅。
 * 需要注意的是使用retryWhen的时候,因为每次重新订阅都会产生错误，所以作为参数的obserbvable会不断地发射数据，使用zipWith操作符可以限制重新订阅的次数，否则会无限制地重新订阅。
 */
public class RetryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("retry");
        mLButton.setOnClickListener(e -> retryObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("retry-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("retry-onError:" + e.getMessage()+"\n");
            }

            @Override
            public void onNext(Integer s) {
                log("retry-onNext:" + s);
            }
        }));
        mRButton.setText("retryWhen");
        mRButton.setOnClickListener(e -> retryWhenObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                log("retryWhen-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("retryWhen-onError:" + e.getMessage()+"\n");
            }

            @Override
            public void onNext(Integer s) {
                log("retryWhen-onNext:" + s);
            }
        }));
    }

    private Observable<Integer> retryObserver() {
        return createObserver().retry(2);
    }

    private Observable<Integer> retryWhenObserver() {
        return createObserver().retryWhen(observable -> observable.zipWith(Observable.just(1, 2, 3),
                (throwable, integer) -> throwable.getMessage() + integer)
                .flatMap(throwable -> {
                    log(throwable);
                    return Observable.timer(1, TimeUnit.SECONDS);
                }));

    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                log("subscribe");
                for (int i = 0; i < 3; i++) {
                    if (i == 2) {
                        subscriber.onError(new Exception("Exception-"));
                    } else {
                        subscriber.onNext(i);
                    }
                }
            }
        });
    }

}
