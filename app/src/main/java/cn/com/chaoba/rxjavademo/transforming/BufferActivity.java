package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class BufferActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("buffer");
        mLButton.setOnClickListener(e -> bufferObserver().subscribe(i -> log("buffer:" + i)));
        mRButton.setText("bufferTime");
        mRButton.setOnClickListener(e -> bufferTimeObserver().subscribe(i -> log("bufferTime:" + i)));
    }

    private Observable<List<Integer>> bufferObserver() {
        //Buffer操作符所要做的事情就是将数据安装规定的大小做一下缓存，然后将缓存的数据作为一个集合发射出去
        //我们加入了一个skip参数用来指定每次发射一个集合需要跳过几个数据，图中如何指定count为2，skip为3
        // ，就会每3个数据发射一个包含两个数据的集合，如果count==skip的话，我们就会发现其等效于第一种情况了。
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).buffer(2, 3);
    }

    private Observable<List<Long>> bufferTimeObserver() {
        //buffer不仅仅可以通过数量规则来缓存，还可以通过时间等规则来缓存，如规定3秒钟缓存发射一次
        return Observable.interval(1, TimeUnit.SECONDS).buffer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());
    }

}
