package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

/**
 * TakeUntil和TakeWhile操作符可以说和SkipUnitl和SkipWhile操作符是完全相反的功能。
 * TakeUntil也是使用一个标志Observable是否发射数据来判断，当标志Observable没有发射数据时，正常发射数据
 * ，而一旦标志Observable发射过了数据则后面的数据都会被丢弃。
 */

public class TakeUntilTakeWhileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("takeUntil");
        mLButton.setOnClickListener(e -> takeUntilObserver().subscribe(i -> log("takeUntil:" + i)));
        mRButton.setText("takeWhile");
        mRButton.setOnClickListener(e -> takeWhileObserver().subscribe(i -> log("takeWhile:" + i)));
    }

    private Observable<Long> takeUntilObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).takeUntil(Observable.timer(3, TimeUnit.SECONDS));
    }

    private Observable<Long> takeWhileObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(aLong -> aLong < 5);
    }
}


