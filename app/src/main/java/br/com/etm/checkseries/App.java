package br.com.etm.checkseries;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import br.com.etm.checkseries.api.ApiFanArt;
import br.com.etm.checkseries.api.ApiTraktTv;
import br.com.etm.checkseries.di.components.AppComponent;
import br.com.etm.checkseries.di.components.DaggerAppComponent;
import br.com.etm.checkseries.di.modules.AppModule;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eduardo on 07/12/17.
 */

public class App extends MultiDexApplication {

    public static Context context;
    private static AppComponent appComponent;
    private static Retrofit.Builder builder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.API_URL);

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initComponent();
    }

    public static Context getContext() {
        return context;
    }

    public static ApiTraktTv getApi() {
        changeApiBaseUrl(BuildConfig.API_URL);
        return builder.build().create(ApiTraktTv.class);
    }

    public static ApiFanArt getApiFanArt() {
        changeApiBaseUrl(BuildConfig.API_FANART_URL);
        return builder.build().create(ApiFanArt.class);
    }

    private static void changeApiBaseUrl(String newApiBaseUrl) {
        builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(newApiBaseUrl);
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
        appComponent.inject(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
