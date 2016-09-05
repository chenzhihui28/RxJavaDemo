package cn.com.chaoba.rxjavademo.conditional_boolean;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

/**
 * SequenceEqual操作符用来判断两个Observable发射的数据序列是否相同（发射的数据相同，数据的序列相同，结束的状态相同），如果相同返回true，否则返回false
 */
public class SequenceEqualActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("equal");
        mLButton.setOnClickListener(e -> equalObserver().subscribe(i -> log("equal:" + i)));
        mRButton.setText("notequal");
        mRButton.setOnClickListener(e -> notEqualObserver().subscribe(i -> log("notequal:" + i)));
    }

    private Observable<Boolean> equalObserver() {
        return Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3));
    }

    private Observable<Boolean> notEqualObserver() {
        return Observable.sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2));
    }
}


