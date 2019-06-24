package kubsu.timetable.retrofit;

import kubsu.timetable.api.ApiTimetable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static kubsu.timetable.utility.Constant.BASE_URL;

public class NetworkService {
    private static NetworkService networkServiceInstance;

    private Retrofit retrofit;

    private NetworkService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
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


