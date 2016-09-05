package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import java.util.ArrayList;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ScanActivity extends BaseActivity {
    /**
     * Scan操作符对一个序列的数据应用一个函数，并将这个函数的结果发射出去作为下个数据应用这个函数时候的第一个参数使用，有点类似于递归操作
     */
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            list.add(2);
        }
        mLButton.setText("scan");
        mLButton.setOnClickListener(e -> scanObserver().subscribe(i -> log("scan:" + i)));
    }

    //输出2的次幂 2 4 8 16 ....
    private Observable<Integer> scanObserver() {
        return Observable.from(list).scan((x, y) -> x * y).observeOn(AndroidSchedulers.mainThread());
    }

}

