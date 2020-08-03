package stefanserkhir.randomuserapp.repository;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stefanserkhir.randomuserapp.interfaces.api.RandomUserApi;

public class RandomUsersRepository {
    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static RandomUserApi sRandomUserApi = sRetrofit.create(RandomUserApi.class);
    private Callback<RandomUsers> mRandomUsersCallback;

    public RandomUsersRepository(Callback<RandomUsers> randomUsersCallback) {
        mRandomUsersCallback = randomUsersCallback;
    }

    public void fetchRandomUsersAsync(int page) {
        sRandomUserApi.getUsers(10, page,
                "json", 1).enqueue(mRandomUsersCallback);
    }
}
