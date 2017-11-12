package luocaca.rxretrofit.bean;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class Person {


    public String name = "么么哒";

    @Inject
    public Person() {

    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
