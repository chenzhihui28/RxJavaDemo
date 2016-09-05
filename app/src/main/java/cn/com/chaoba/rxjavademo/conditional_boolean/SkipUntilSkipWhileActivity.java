package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

/**
 * 这两个操作符都是根据条件来跳过一些数据，不同之处在于SkipUntil是根据一个标志Observable来判断的
 * ，当这个标志Observable没有发射数据的时候，所有源Observable发射的数据都会被跳过；
 * 当标志Observable发射了一个数据，则开始正常地发射数据
 */
public class SkipUntilSkipWhileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("skipUntil");
        mLButton.setOnClickListener(e -> skipUntilObserver().subscribe(i -> log("skipUntil:" + i)));
        mRButton.setText("skipWhile");
        mRButton.setOnClickListener(e -> skipWhileObserver().subscribe(i -> log("skipWhile:" + i)));
    }

    private Observable<Long> skipUntilObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).skipUntil(Observable.timer(3, TimeUnit.SECONDS));
    }

    private Observable<Long> skipWhileObserver() {
        return Observable.interval(1, TimeUnit.SECONDS).skipWhile(aLong -> aLong < 5);
    }
}


