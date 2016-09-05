package cn.com.chaoba.rxjavademo.errorhandling;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

public class OnErrorActivity extends BaseActivity {

    /**
     * Catch类似于java 中的try/catch，当错误发生的时候，可以拦截对onError的调用，让Observable不会因为错误的产生而终止
     * 。在Rxjava中，将这个操作符实现为3个操作符，分别是：
     * 1.OnErrorReturn-当发生错误的时候，让Observable发射一个预先定义好的数据并正常地终止
     * 2.OnErrorResume-当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
     * 3.OnExceptionResumeNext-类似于OnErrorResume,不同之处在于其会对onError抛出的数据类型做判断
     * ，如果是Exception，也会使用另外一个Observable代替原Observable继续发射数据，否则会将错误分发给Subscriber。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("onErrorReturn");
        mLButton.setOnClickListener(e -> onErrorReturnObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorReturn-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorReturn-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorReturn-onNext:" + s);
            }
        }));
        mRButton.setText("onErrorResume");
        mRButton.setOnClickListener(e -> onErrorResumeNextObserver().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onErrorResume-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onErrorResume-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onErrorResume-onNext:" + s);
            }
        }));
    }

    private Observable<String> onErrorReturnObserver() {
        return createObserver().onErrorReturn(throwable -> "onErrorReturn");
    }

    private Observable<String> onErrorResumeNextObserver() {
        return createObserver().onErrorResumeNext(Observable.just("7", "8", "9"));
    }

    private Observable<String> createObserver() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i <= 6; i++) {
                    if (i < 3) {
                        subscriber.onNext("onNext:" + i);
                    } else {
                        subscriber.onError(new Throwable("Throw error"));
                    }
                }
            }
        });
    }
}
