package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;

/**
 * Contains操作符用来判断源Observable所发射的数据是否包含某一个数据，如果包含会返回true，如果源Observable已经结束了却还没有发射这个数据则返回false。
 * IsEmpty操作符用来判断源Observable是否发射过数据，如果发射过就会返回false，如果源Observable已经结束了却还没有发射这个数据则返回true。
 */
public class ContainsActivity extends BaseActivity {
    boolean tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("contains");
        mLButton.setOnClickListener(e -> containsObserver().subscribe(i -> log("contains:" + i)));
        mRButton.setText("isEmpty");
        mRButton.setOnClickListener(e -> defaultObserver().subscribe(i -> log("isEmpty:" + i)));
    }

    private Observable<Boolean> containsObserver() {
        if (tag) {
            return Observable.just(1, 2, 3).contains(3);
        }
        tag = true;
        return Observable.just(1, 2, 3).contains(4);
    }

    private Observable<Boolean> defaultObserver() {

        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        }).isEmpty();
    }

    private Observable<Integer> defaultIfEmptyObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        }).defaultIfEmpty(666);
    }};



