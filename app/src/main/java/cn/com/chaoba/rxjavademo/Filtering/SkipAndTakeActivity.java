package cn.com.chaoba.rxjavademo.filtering;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

public class SkipAndTakeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("Skip");
        mLButton.setOnClickListener(e -> skipObserver().subscribe(i -> log("Skip:" + i)));
        mRButton.setText("Take");
        mRButton.setOnClickListener(e -> takeObserver().subscribe(i -> log("Take:" + i)));
    }

    //Skip操作符将源Observable发射的数据过滤掉前n项，而Take操作符则只取前n项，理解和使用起来都很容易，但是用处很大
    // 。另外还有SkipLast和TakeLast操作符，分别是从后面进行过滤操作。
    private Observable<Integer> skipObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).skip(2);
    }
    private Observable<Integer> takeObserver() {
        return Observable.just(0, 1, 2, 3, 4, 5).take(2);
    }


}
