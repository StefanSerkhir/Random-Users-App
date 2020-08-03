package stefanserkhir.randomuserapp.presenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import stefanserkhir.randomuserapp.interfaces.presenter.RandomUsersPresenter;
import stefanserkhir.randomuserapp.interfaces.ui.RandomUsersView;
import stefanserkhir.randomuserapp.interfaces.ui.RepositoryItemView;
import stefanserkhir.randomuserapp.model.RandomUser;
import stefanserkhir.randomuserapp.repository.RandomUsersRepository;
import stefanserkhir.randomuserapp.repository.RandomUsers;
import stefanserkhir.randomuserapp.ui.RandomUsersActivity;

public class RandomUsersPresenterImpl implements RandomUsersPresenter, Callback<RandomUsers> {
    private List<RandomUser> mRandomUsers = new ArrayList<>();
    private int mLoadingPage = 1;
    private boolean mLoading;
    private RandomUsersView mUpdateUICallback;

    public RandomUsersPresenterImpl(RandomUsersActivity updateUICallback) {
        mUpdateUICallback = updateUICallback;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean isLoading) {
        mLoading = isLoading;
    }

    public void onBindRepositoryItemViewAtPosition(int position, RepositoryItemView itemView) {
        RandomUser randomUser = mRandomUsers.get(position);
        itemView.setUserNumber(position);
        itemView.setMask(randomUser.getGender(), randomUser.getPicture().getLarge());
        itemView.setUserFullName(randomUser.getFullName());
    }

    public int getRepositoriesItemsCount() {
        return mRandomUsers.size();
    }

    @Override
    public void fetchRandomUsers() {
        new RandomUsersRepository(this).fetchRandomUsersAsync(mLoadingPage);
    }

    @Override
    public void onResponse(Call<RandomUsers> call, Response<RandomUsers> response) {
        if (response.body() != null) {
            mLoadingPage++;
            setLoading(false);
            boolean wayUpdate;
            if (mRandomUsers.size() == 0 | mLoadingPage <= 2) {
                wayUpdate = true;
                mRandomUsers = response.body().getResults();
            } else {
                wayUpdate = false;
                mRandomUsers.addAll(response.body().getResults());
            }

            mUpdateUICallback.updateUI(wayUpdate);
        }
    }

    @Override
    public void onFailure(Call<RandomUsers> call, Throwable t) {
        // TODO Crate UI Method like "onErrorFetchingUsers"
    }
}
