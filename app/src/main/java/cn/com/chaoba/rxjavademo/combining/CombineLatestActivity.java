package cn.com.chaoba.rxjavademo.combining;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CombineLatestActivity extends BaseActivity {

    /**
     CombineLatest操作符可以将2~9个Observable发射的数据组装起来然后再发射出来。不过还有两个前提：
     所有的Observable都发射过数据。
     满足条件1的时候任何一个Observable发射一个数据，就将所有Observable最新发射的数据按照提供的函数组装起来发射出去。
     Rxjava实现CombineLast操作符可以让我们直接将组装的Observable作为参数传值，也可以将所有的Observable装在一个List里面传进去
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("combineList");
        mLButton.setOnClickListener(e -> combineListObserver().subscribe(i -> log("combineList:" + i)));
        mRButton.setText("CombineLatest");
        mRButton.setOnClickListener(e -> combineLatestObserver().subscribe(i -> log("CombineLatest:" + i)));
    }

    private Observable<Integer> createObserver(int index) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 1; i < 6; i++) {
                    subscriber.onNext(i * index);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }

    private Observable<Integer> combineLatestObserver() {
        return Observable.combineLatest(createObserver(1), createObserver(2), (num1, num2) -> {
            log("left:" + num1 + " right:" + num2);
            return num1 + num2;
        });
    }

    List<Observable<Integer>> list = new ArrayList<>();

    private Observable<Integer> combineListObserver() {
        for (int i = 1; i < 5; i++) {
            list.add(createObserver(i));
        }
        return Observable.combineLatest(list, args -> {
            int temp = 0;
            for (Object i : args) {
                log(i);
                temp += (Integer) i;
            }
            return temp;
        });
    }


}
