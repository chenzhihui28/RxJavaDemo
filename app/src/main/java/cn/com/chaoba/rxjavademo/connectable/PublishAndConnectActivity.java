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
 * Connectable Observable: 就是一种特殊的Observable对象，并不是Subscrib的时候就发射数据，
 * 而是只有对其应用connect操作符的时候才开始发射数据，所以可以用来更灵活的控制数据发射的时机。
 * 而Publish操作符就是用来将一个普通的Observable对象转化为一个Connectable Observable。
 * 需要注意的是如果发射数据已经开始了再进行订阅只能接收以后发射的数据。
 * Connect操作符就是用来触发Connectable Observable发射数据的。应用Connect操作符后会返回一个Subscription对象
 * ，通过这个Subscription对象，我们可以调用其unsubscribe方法来终止数据的发射
 * 。另外，如果还没有订阅者订阅的时候就应用Connect操作符也是可以使其开始发射数据的。
 */
public class PublishAndConnectActivity extends BaseActivity {
    Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectableObservable<Long> obs = publishObserver();
        Action1 action2 = o -> log("action2:" + o);
        Action1 action1 = o -> {
            log("action1:" + o);
            if ((long) o == 3) obs.subscribe(action2);
        };
        obs.subscribe(action1);

        mLButton.setText("start");
        mLButton.setOnClickListener(e -> mSubscription = obs.connect());
        mRButton.setText("stop");
        mRButton.setOnClickListener(e -> {
            if (mSubscription != null) {
                mSubscription.unsubscribe();
            }
        });
    }

    /**
     * 当我们点击start按钮的时候对这个Connectable Observable 对象应用connect操作符，让其开始发射数据。
     * 当发射到3的时候将action2给订阅上，这个两个订阅者将同时收到相同的数据。点击stop按钮的时候终止其数据的发射。
     */
    private ConnectableObservable<Long> publishObserver() {
        Observable<Long> obser = Observable.interval(1, TimeUnit.SECONDS);
        obser.observeOn(Schedulers.newThread());
        return obser.publish();
    }

}
