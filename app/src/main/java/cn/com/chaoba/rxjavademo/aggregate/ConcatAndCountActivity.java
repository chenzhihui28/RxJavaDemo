package cn.com.chaoba.rxjavademo.aggregate;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

/**
 * Concat操作符将多个Observable结合成一个Observable并发射数据，并且严格按照先后顺序发射数据，前一个Observable的数据没有发射完，是不能发射后面Observable的数据的。
 有两个操作符跟它类似，但是有区别，分别是
 1.startWith：仅仅是在前面插上一个数据。
 2.merge:其发射的数据是无序的。

 Count操作符用来统计源Observable发射了多少个数据，最后将数目给发射出来；如果源Observable发射错误，则会将错误直接报出来；在源Observable没有终止前，count是不会发射统计数据的
 */
public class ConcatAndCountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("concat");
        mLButton.setOnClickListener(e -> concatObserver().subscribe(i -> log("concat:" + i)));
        mRButton.setText("count");
        mRButton.setOnClickListener(e -> countObserver().subscribe(i -> log("count:" + i)));
    }

    private Observable<Integer> concatObserver() {
        Observable<Integer> obser1 = Observable.just(1, 2, 3);
        Observable<Integer> obser2 = Observable.just(4, 5, 6);
        Observable<Integer> obser3 = Observable.just(7, 8, 9);
        return Observable.concat(obser1, obser2, obser3);
    }
    private Observable<Integer> countObserver() {
        return Observable.just(1, 2, 3).count();
    }
}


