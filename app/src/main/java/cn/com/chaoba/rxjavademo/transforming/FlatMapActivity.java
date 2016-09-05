package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

public class FlatMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("flatMap");
        mLButton.setOnClickListener(e -> flatMapObserver().subscribe(i -> log(i)));
        mRButton.setText("flatMapIterable");
        mRButton.setOnClickListener(e -> flatMapIterableObserver().subscribe(i -> log("flatMapIterable:" + i)));
    }

    private Observable<String> flatMapObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).flatMap(integer -> {
            //加入timer,一秒钟后输出奇数,两秒后输出偶数
            final int oo = integer;
            if (integer % 2 == 0) {
                return Observable.timer(2, TimeUnit.SECONDS).flatMap(i -> Observable.just(oo + ""));
            }else {
                return Observable.timer(1, TimeUnit.SECONDS).flatMap(i -> Observable.just(oo + ""));
            }
        });

//原来的
//        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).flatMap(integer -> {
//            return Observable.just("flat map:" + integer);
//        });

    }

    private Observable<? extends Integer> flatMapIterableObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .flatMapIterable(
                        integer -> {
                            ArrayList<Integer> s = new ArrayList<>();
                            for (int i = 0; i < integer; i++) {
                                s.add(integer);
                            }
                            return s;
                        }
                );
    }


}