package luocaca.rxretrofit;

import org.junit.Test;

import javax.inject.Inject;

import luocaca.rxretrofit.http.Api;
import retrofit2.Retrofit;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";

    @Inject
    Retrofit retrofit ;
    @Test
    public void addition_isCorrect() throws Exception {



        assertEquals(4, 2 + 2);


        System.out.print("hello world"+(2+10));

        retrofit.create(Api.class);


    }
}