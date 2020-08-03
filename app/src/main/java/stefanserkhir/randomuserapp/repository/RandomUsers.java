package stefanserkhir.randomuserapp.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import stefanserkhir.randomuserapp.model.RandomUser;

public class RandomUsers {
    @SerializedName("results")
    @Expose
    private List<RandomUser> results = null;

    public List<RandomUser> getResults() {
        return results;
    }

    public void setResults(List<RandomUser> results) {
        this.results = results;
    }
}
