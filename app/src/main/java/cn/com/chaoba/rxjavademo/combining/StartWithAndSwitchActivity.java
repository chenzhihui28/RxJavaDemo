package cn.com.chaoba.rxjavademo.combining;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class StartWithAndSwitchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("StartWith");
        mLButton.setOnClickListener(e -> startWithObserver().subscribe(i -> log("StartWith:" + i)));
        mRButton.setText("switch");
        mRButton.setOnClickListener(e -> switchObserver().subscribe(i -> log("switch:" + i)));
    }

    private Observable<Integer> startWithObserver() {
        return Observable.just(1, 2, 3).startWith(-1, 0);
    }

    /**
     * switch操作符在Rxjava上的实现为switchOnNext,用来将一个发射多个小Observable的源Observable转化为一个Observable，然后发射这多个小Observable所发射的数据。
     需要注意的就是，如果一个小的Observable正在发射数据的时候，源Observable又发射出一个新的小Observable，则前一个Observable发射的数据会被抛弃，直接发射新
     的小Observable所发射的数据
     使用siwtch的时候第一个小Observable只发射出了两个数据，第二个小Observable就被源Observable发射出来了，所以其接下来的两个数据被丢弃。
     */
    private Observable<String> switchObserver() {
        return Observable.switchOnNext(Observable.create(
                new Observable.OnSubscribe<Observable<String>>() {
                    @Override
                    public void call(Subscriber<? super Observable<String>> subscriber) {
                        for (int i = 1; i < 3; i++) {
                            subscriber.onNext(createObserver(i));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ));
    }

    private Observable<String> createObserver(int index) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i < 5; i++) {
                    subscriber.onNext(index + "-" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }


}
