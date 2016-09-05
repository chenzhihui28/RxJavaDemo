package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("First");
        mLButton.setOnClickListener(e -> firstObserver().subscribe(i -> log("First:" + i )));
        mRButton.setText(" Blocking");
        mRButton.setOnClickListener(e -> {
            log(Thread.currentThread().getName()+" blocking:" + filterObserver().first(i -> i > 1));
        });
    }

    //First操作符只会返回第一条数据，并且还可以返回满足条件的第一条数据
    private Observable<Integer> firstObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).first(i -> i > 1);
    }

    /**
     * BlockingObservable方法,这个方法不会对Observable做任何处理，只会阻塞住，
     * 当满足条件的数据发射出来的时候才会返回一个BlockingObservable对象。
     * 可以使用Observable.toBlocking或者BlockingObservable.from方法来将一个Observable对象转化为BlockingObservable对象。
     * BlockingObservable可以和first操作符进行配合使用。
     */
    private BlockingObservable<Integer> filterObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                log(Thread.currentThread().getName());
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!subscriber.isUnsubscribed()) {
                        log("onNext:" + i);
                        subscriber.onNext(i);
                    }
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.computation()).toBlocking();
    }

}
