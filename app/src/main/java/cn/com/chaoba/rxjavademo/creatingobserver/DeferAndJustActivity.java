package cn.com.chaoba.rxjavademo.creatingobserver;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

public class DeferAndJustActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable<Long> deferObservable = DeferObserver();
        Observable<Long> justObservable = JustObserver();
        mLButton.setText("Defer");
        mRButton.setText("Just");
        mLButton.setOnClickListener(e -> deferObservable.subscribe(time -> log("defer:" + time)));
        mRButton.setOnClickListener(e -> justObservable.subscribe(time -> log("just:" + time)));
    }

    private Observable<Long> DeferObserver() {
        //Defer操作符只有当有Subscriber来订阅的时候才会创建一个新的Observable对象,
        // 也就是说每次订阅都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的
        return Observable.defer(() -> Observable.just(System.currentTimeMillis()));
    }

    private Observable<Long> JustObserver() {
        //Just操作符将某个对象转化为Observable对象，并且将其发射出去
        // ，可以使一个数字、一个字符串、数组、Iterate对象等，是一种非常快捷的创建Observable对象的方法
        return Observable.just(System.currentTimeMillis());
    }


}
