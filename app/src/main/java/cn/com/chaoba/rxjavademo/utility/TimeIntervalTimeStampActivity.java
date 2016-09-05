package cn.com.chaoba.rxjavademo.utility;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;
import rx.schedulers.Timestamped;

public class TimeIntervalTimeStampActivity extends BaseActivity {

    /**
     * TimeInterval会拦截发射出来的数据，取代为前后两个发射两个数据的间隔时间。
     * 对于第一个发射的数据，其时间间隔为订阅后到首次发射的间隔。
     * TimeStamp会将每个数据项给重新包装一下，加上了一个时间戳来标明每次发射的时间
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("timeInterval");
        mLButton.setOnClickListener(e -> timeIntervalObserver().subscribe(i -> log("timeInterval:" + i.getValue()+"-"+i.getIntervalInMilliseconds())));
        mRButton.setText("timeStamp");
        mRButton.setOnClickListener(e -> timeStampObserver().subscribe(i -> log("timeStamp:" + i.getValue()+"-"+i.getTimestampMillis())));
    }

    private Observable<TimeInterval<Integer>> timeIntervalObserver() {
        return createObserver().timeInterval();
    }

    private Observable<Timestamped<Integer>> timeStampObserver() {
        return createObserver().timestamp();
    }

    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i <= 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread());
    }
}
