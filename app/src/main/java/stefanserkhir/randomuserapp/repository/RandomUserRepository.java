package stefanserkhir.randomuserapp.repository;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stefanserkhir.randomuserapp.model.RandomUser;
import stefanserkhir.randomuserapp.repository.api.RandomUserApi;

public class RandomUserRepository {
    private RandomUsers sRandomUsers;
    private static Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static RandomUserApi sRandomUserApi = sRetrofit.create(RandomUserApi.class);

    public void fetchRandomUsers(int page) {
        try {
            Response<RandomUsers> response = sRandomUserApi.getUsers(10, page,
                    "json", 1).execute();
            sRandomUsers = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<RandomUser> getData(int page) {
        fetchRandomUsers(page);
        List<RandomUser> randomUsers = new ArrayList<>();
        for (stefanserkhir.randomuserapp.repository.model.RandomUser randomUserRepo : sRandomUsers.getResults()) {
            randomUsers.add(convertModel(randomUserRepo));
        }
        return randomUsers;
    }

    private RandomUser convertModel(stefanserkhir.randomuserapp.repository.model.RandomUser randomUserRepo) {
        RandomUser randomUser = new RandomUser();
        randomUser.setTitle(randomUserRepo.getName().getTitle());
        randomUser.setFirstName(randomUserRepo.getName().getFirst());
        randomUser.setLastName(randomUserRepo.getName().getLast());
        randomUser.setGender(randomUserRepo.getGender());
        randomUser.setAvatarURL(randomUserRepo.getPicture().getLarge());
        return randomUser;
    }
}
