package stefanserkhir.randomuserapp.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stefanserkhir.randomuserapp.model.RandomUser;
import stefanserkhir.randomuserapp.repository.api.RandomUserApi;

public class RandomUserRepository {
    private RandomUsers mRandomUsers;
    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static RandomUserApi sRandomUserApi = sRetrofit.create(RandomUserApi.class);

    public List<RandomUser> fetchRandomUsers(int page) {
        try {
            Response<RandomUsers> response = sRandomUserApi.getUsers(10, page,
                    "json", 1).execute();
            mRandomUsers = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mRandomUsers.getResults();
    }
}
