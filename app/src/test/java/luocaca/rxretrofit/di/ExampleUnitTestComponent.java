package luocaca.rxretrofit.di;

import javax.inject.Singleton;

import dagger.Component;
import luocaca.rxretrofit.ExampleUnitTest;
import luocaca.rxretrofit.di.module.ClientModel;

/**
 * Created by Administrator on 2017/11/19 0019.
 */

@Singleton
@Component(modules = ClientModel.class)
public interface ExampleUnitTestComponent {
    void inject(ExampleUnitTest unitTest);
}
