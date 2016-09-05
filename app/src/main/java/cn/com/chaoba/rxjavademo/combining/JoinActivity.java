package cn.com.chaoba.rxjavademo.combining;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class JoinActivity extends BaseActivity {


    /**
     Join操作符根据时间窗口来组合两个Observable发射的数据，每个Observable都有一个自己的时间窗口，要组合的时候，在这个时间窗口内的数据都有有效的，可以拿来组合。
     Rxjava还实现了groupJoin，基本和join相同，只是最后组合函数的参数不同
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("join");
        mLButton.setOnClickListener(e -> joinObserver().subscribe(i -> log("join:" + i + "\n")));
        mRButton.setText("groupJoin");
        mRButton.setOnClickListener(e -> groupJoinObserver().subscribe(i -> i.subscribe(j -> log("groupJoin:" + j + "\n"))));
    }

    private Observable<String> createObserver() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 1; i < 5; i++) {
                    subscriber.onNext("Right-" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }


    /**
     * 使用join操作符需要4个参数，分别是：
     源Observable所要组合的目标Observable,也就是这里的CreateObserver(),源Observable就是just("Left-")
     一个函数，就收从源Observable发射来的数据，并返回一个Observable，这个Observable的生命周期决定了源Observable发射出来数据的有效期,也就是just的有效期是三秒
     一个函数，就收从目标Observable发射来的数据，并返回一个Observable，这个Observable的生命周期决定了目标Observable发射出来数据的有效期,也就是createObserver的有效期是两秒
     一个函数，接收从源Observable和目标Observable发射来的数据，并返回最终组合完的数据。在这里是将他们相加
     */
    private Observable<String> joinObserver() {
        return Observable.just("Left-").join(createObserver(),
                integer -> Observable.timer(3000, TimeUnit.MILLISECONDS),
                integer -> Observable.timer(2000, TimeUnit.MILLISECONDS),
                (i, j) -> i + j
        );
    }

    private Observable<Observable<String>> groupJoinObserver() {
        return Observable.just("Left-")
                .groupJoin(createObserver(),
                        s -> Observable.timer(3000, TimeUnit.MILLISECONDS),
                        s -> Observable.timer(2000, TimeUnit.MILLISECONDS),
                        (s, stringObservable) -> stringObservable.map(str -> s + str));
    }


}
