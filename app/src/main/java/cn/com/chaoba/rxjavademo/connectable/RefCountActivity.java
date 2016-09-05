package cn.com.chaoba.rxjavademo.connectable;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * RefCount操作符就是将一个Connectable Observable 对象再重新转化为一个普通的Observable对象
 * ，这时候如果由订阅者进行订阅将会触发数据的发射。
 */
public class RefCountActivity extends BaseActivity {
    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectableObservable<Long> obs = publishObserver();

        mLButton.setText("refCount");
        mLButton.setOnClickListener(e -> subscription = obs.refCount().subscribe(aLong -> {
            log("refCount:" + aLong);
        }));
        mRButton.setText("stop");
        mRButton.setOnClickListener(e -> subscription.unsubscribe());
    }

    private ConnectableObservable<Long> publishObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.publish();
    }

}
