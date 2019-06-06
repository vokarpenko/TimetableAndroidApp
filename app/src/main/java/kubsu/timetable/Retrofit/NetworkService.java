package kubsu.timetable.Retrofit;

import android.util.JsonReader;
import android.webkit.CookieManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookiePolicy;

import kubsu.timetable.Api.ApiTimetable;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static kubsu.timetable.Utility.Constant.BASE_URL;

public class NetworkService {

    private static NetworkService networkServiceInstance;

    private Retrofit retrofit;

    private NetworkService(final String  token) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // init cookie manager
        //Create a new Interceptor.
        Interceptor headerAuthorizationInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();
                Headers headers = request.headers().newBuilder().add("Authorization","Bearer "+token).build();
                request = request.newBuilder().headers(headers).build();
                return chain.proceed(request);
            }
        };
        //Add the interceptor to the client builder.
        clientBuilder
                .addInterceptor(headerAuthorizationInterceptor);

        retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private NetworkService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    private NetworkService(boolean useGSONConverter){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static NetworkService getInstance(boolean useGSONConverter) {
        if (networkServiceInstance == null) {
            networkServiceInstance = new NetworkService(useGSONConverter);
        }
        return networkServiceInstance;
    }
    public static NetworkService getInstance() {
        if (networkServiceInstance == null) {
            networkServiceInstance = new NetworkService();
        }
        return networkServiceInstance;
    }
    public ApiTimetable getApi(){
        return retrofit.create(ApiTimetable.class);
    }

}


