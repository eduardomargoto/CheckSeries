package br.com.etm.checkseries;

import android.support.multidex.MultiDexApplication;

import br.com.etm.checkseries.api.ApiTraktTv;
import br.com.etm.checkseries.di.AppComponent;
import br.com.etm.checkseries.di.AppModule;
import br.com.etm.checkseries.di.DaggerAppComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eduardo on 07/12/17.
 */

public class App extends MultiDexApplication {

    private static AppComponent appComponent;
    private static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_URL)
                .build();

        initComponent();
    }

    public static ApiTraktTv getApi() {
        return retrofit.create(ApiTraktTv.class);
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
        appComponent.inject(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
