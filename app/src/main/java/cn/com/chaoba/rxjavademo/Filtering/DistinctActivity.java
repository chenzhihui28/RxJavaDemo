package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

public class DistinctActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("distinct");
        mLButton.setOnClickListener(e -> distinctObserver().subscribe(i -> log("distinct:" + i)));
        mRButton.setText("UntilChanged");
        mRButton.setOnClickListener(e -> distinctUntilChangedObserver().subscribe(i -> log("UntilChanged:" + i)));
    }

    //简单的去重
    private Observable<Integer> distinctObserver() {
        //输出12345
        return Observable.just(1, 2, 3, 4, 5, 4, 3, 2, 1).distinct();

    }

    //过滤连续的重复数据
    private Observable<Integer> distinctUntilChangedObserver() {
        return Observable.just(1, 2, 3, 3, 3, 1, 2, 3, 3).distinctUntilChanged();
        //输出123123

    }


}
