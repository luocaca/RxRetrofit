package luocaca.rxretrofit.di.component;

import javax.inject.Singleton;

import dagger.Component;
import luocaca.rxretrofit.di.module.ClientModel;
import luocaca.rxretrofit.ui.BookListActivity1;

/**
 * Created by Administrator on 2017/10/18 0018.
 */

@Singleton
@Component(modules = ClientModel.class)
public interface BookList1Component {
    void inject(BookListActivity1 activity);
}
