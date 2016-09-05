package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.Subscriber;
import rx.observables.GroupedObservable;

public class GroupbyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("groupBy");
        mLButton.setOnClickListener(e -> groupByObserver().subscribe(new Subscriber<GroupedObservable<Integer, Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GroupedObservable<Integer, Integer> groupedObservable) {
                groupedObservable.count().subscribe(integer -> log("key" + groupedObservable.getKey() + " contains:" + integer + " numbers"));
            }
        }));
        mRButton.setText("groupByKeyValue");
        mRButton.setOnClickListener(e -> groupByKeyValueObserver().subscribe(new Subscriber<GroupedObservable<Integer, String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GroupedObservable<Integer, String> integerIntegerGroupedObservable) {
                if (integerIntegerGroupedObservable.getKey() == 0) {
                    integerIntegerGroupedObservable.subscribe(integer -> log(integer));
                }
            }
        }));
    }

    private Observable<GroupedObservable<Integer, Integer>> groupByObserver() {
        //GroupBy操作符将原始Observable发射的数据按照key来拆分成一些小的Observable，然后这些小的Observable分别发射其所包含的的数据，类似于sql里面的groupBy。
        //在使用中，我们需要提供一个生成key的规则，所有key相同的数据会包含在同一个小的Observable种。另外我们还可以提供一个函数来对这些数据进行转化，有点类似于集成了flatMap。
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).groupBy(integer -> integer % 2);
    }

    private Observable<GroupedObservable<Integer, String>> groupByKeyValueObserver() {
        //创建两个经过groupBy转化的Observable对象，第一个按照奇数偶数分组，第二个分组后将数字加上一个字符串前缀
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy(integer -> integer % 2, integer -> "groupByKeyValue:" + integer);
    }
}