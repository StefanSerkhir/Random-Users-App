package stefanserkhir.randomuserapp.repository.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import stefanserkhir.randomuserapp.repository.RandomUsers;

public interface RandomUserApi {
    @GET("api/")
    Call<RandomUsers> getUsers(@Query("results") int numResults,
                               @Query("page") int page,
                               @Query("format") String format,
                               @Query("noinfo") int noInfo);
}
