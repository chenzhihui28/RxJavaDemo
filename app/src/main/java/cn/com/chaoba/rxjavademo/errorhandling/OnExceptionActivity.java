package cn.com.chaoba.rxjavademo.errorhandling;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * OnExceptionResumeNext-类似于OnErrorResume,不同之处在于其会对onError抛出的数据类型做判断
 * 如果是Exception,也会使用另外一个Observable代替原来的Observable继续发射数据,否则将会把错误分发给Subscriber处理
 */
public class OnExceptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("onException-true");
        mLButton.setOnClickListener(e -> onExceptionResumeObserver(true).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onException-true-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onException-true-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onException-true-onNext:" + s);
            }
        }));
        mRButton.setText("onException-false");
        mRButton.setOnClickListener(e -> onExceptionResumeObserver(false).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                log("onException-false-onCompleted\n");
            }

            @Override
            public void onError(Throwable e) {
                log("onException-false-onError:" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                log("onException-false-onNext:" + s);
            }
        }));
    }

    private Observable<String> onExceptionResumeObserver(boolean isException) {
        return createObserver(isException).onExceptionResumeNext(Observable.just("7", "8", "9"));
    }


    private Observable<String> createObserver(Boolean createExcetion) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i <= 6; i++) {
                    if (i < 3) {
                        subscriber.onNext("onNext:" + i);
                    } else if (createExcetion) {
                        subscriber.onError(new Exception("Exception"));
                    } else {
                        subscriber.onError(new Throwable("Throw error"));

                    }
                }
            }
        });
    }
}
