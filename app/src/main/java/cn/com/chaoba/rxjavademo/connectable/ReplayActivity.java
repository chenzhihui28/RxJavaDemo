package cn.com.chaoba.rxjavademo.connectable;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Replay操作符返回一个Connectable Observable 对象并且可以缓存其发射过的数据，
 * 这样即使有订阅者在其发射数据之后进行订阅也能收到其之前发射过的数据。不过使用Replay操作符我们最好还是限定其缓存的大小，
 * 否则缓存的数据太多了可会占用很大的一块内存。对缓存的控制可以从空间和时间两个方面来实现。
 */
public class ReplayActivity extends BaseActivity {
    Subscription mSubscription;
    ConnectableObservable<Long> obs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Action1 action2 = o -> log("action2:" + o);
        Action1 action1 = o -> {
            log("action1:" + o);
            if ((long) o == 5) obs.subscribe(action2);
        };


        mLButton.setText("relayCount");
        mLButton.setOnClickListener(e -> {
            obs = relayCountObserver();
            obs.subscribe(action1);
            log("relayCount");
            mSubscription = obs.connect();
        });
        mRButton.setText("relayTime");
        mRButton.setOnClickListener(e -> {
            obs = relayTimeObserver();
            obs.subscribe(action1);
            log("relayTime");
            mSubscription = obs.connect();
        });
    }

    private ConnectableObservable<Long> relayCountObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.replay(2);
    }

    private ConnectableObservable<Long> relayTimeObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.replay(3, TimeUnit.SECONDS);
    }

}
