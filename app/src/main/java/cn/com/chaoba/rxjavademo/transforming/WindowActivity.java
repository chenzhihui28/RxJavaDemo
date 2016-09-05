package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class WindowActivity extends BaseActivity {

    /**
     * Window操作符类似于我们前面讲过的buffer，不同之处在于window发射的是一些小的Observable对象
     * ，由这些小的Observable对象来发射内部包含的数据。同buffer一样，window不仅可以通过数目来分组还可以通过时间等规则来分组
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("window");
        mLButton.setOnClickListener(e -> windowCountObserver().subscribe(i -> {
            log(i);
            i.subscribe((j -> log("window:" + j)));
        }));
        mRButton.setText("Time");
        mRButton.setOnClickListener(e -> wondowTimeObserver().subscribe(i -> {
            log(i);
            i.observeOn(AndroidSchedulers.mainThread()).subscribe((j -> log("wondowTime:" + j)));
        }));
    }

//    创建两个Observable对象分别使用window的数目和时间规则来进行分组。
    private Observable<Observable<Integer>> windowCountObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).window(3);
    }

    private Observable<Observable<Long>> wondowTimeObserver() {
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }


}