package cn.com.chaoba.rxjavademo.aggregate;

import android.os.Bundle;

import java.util.ArrayList;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

/**
 * Reduce操作符应用一个函数接收Observable发射的数据和函数的计算结果作为下次计算的参数，输出最后的结果。
 * 跟scan操作符很类似，只是scan会输出每次计算的结果，而reduce只会输出最后的结果。
 *
 * Collect操作符类似于Reduce，但是其目的不同，collect用来将源Observable发射的数据给收集到一个数据结构里面，需要使用两个参数：
 一个产生收集数据结构的函数。
 一个接收第一个函数产生的数据结构和源Observable发射的数据作为参数的函数。
 */
public class Reducectivity extends BaseActivity {
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 10; i++) {
            list.add(2);
        }
        mLButton.setText("reduce");
        mLButton.setOnClickListener(e -> reduceObserver().subscribe(i -> log("reduce:" + i)));
        mRButton.setText("collect");
        mRButton.setOnClickListener(e -> collectObserver().subscribe(i -> log("collect:" + i)));
    }

    private Observable<Integer> reduceObserver() {
        return Observable.from(list).reduce((x, y) -> x * y);
    }
    private Observable<ArrayList<Integer>> collectObserver() {
         return Observable.from(list).collect(() -> new ArrayList<>(), (integers, integer) -> integers.add(integer));
    }

}

