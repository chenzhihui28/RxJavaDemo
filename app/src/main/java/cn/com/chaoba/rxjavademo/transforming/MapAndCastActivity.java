package cn.com.chaoba.rxjavademo.transforming;

import android.os.Bundle;

import cn.com.chaoba.rxjavademo.BaseActivity;
import rx.Observable;

public class MapAndCastActivity extends BaseActivity {

    /**
     * Map操作符的功能类似于FlatMap，不同之处在于它对数据的转化是直接进行的，而FlatMap需要通过一些中间的Observables来进行。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLButton.setText("Map");
        mLButton.setOnClickListener(e -> mapObserver().subscribe(i -> log("Map:" + i)));
        mRButton.setText("Cast");
        mRButton.setOnClickListener(e -> castObserver().subscribe(i -> log("Cast:" + i.getName())));
    }

    private Observable<Integer> mapObserver() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9).map(integer -> integer * 10);
    }

    /**
     * Cast将Observable发射的数据强制转化为另外一种类型，属于Map的一种具体的实现
     */
    private Observable<Dog> castObserver() {
        return Observable.just(getAnimal())
                .cast(Dog.class);
    }

    Animal getAnimal() {
        return new Dog();
    }

    class Animal {
        protected String name = "Animal";

        Animal() {
            log("create " + name);
        }

        String getName() {
            return name;
        }
    }

    class Dog extends Animal {
        Dog() {
            name = getClass().getSimpleName();
            log("create " + name);
        }

    }
}
