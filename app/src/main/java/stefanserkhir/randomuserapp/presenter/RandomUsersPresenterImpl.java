package stefanserkhir.randomuserapp.presenter;

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

public class RandomUsersPresenterImpl implements RandomUsersPresenter, Callback<RandomUsers> {
    private static RandomUsersPresenter sRandomUsersPresenter;
    private List<RandomUser> mRandomUsers;
    private int mLoadingPage = 1;
    private boolean mLoading;
    private RandomUsersView mView;

    private RandomUsersPresenterImpl() {}

    public static RandomUsersPresenter getInstance() {
        if (sRandomUsersPresenter == null) {
            sRandomUsersPresenter = new RandomUsersPresenterImpl();
        }
        return sRandomUsersPresenter;
    }

    @Override
    public void onAttachView(RandomUsersView view) {
        mView = view;
        mView.toggleListAndProgressBar(false);
    }

    @Override
    public void setLoading(boolean isLoading) {
        mLoading = isLoading;
    }

    @Override
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public void onUpdatingList(boolean refreshOrUploading) {
        new RandomUsersRepository(this)
                .fetchRandomUsersAsync(mLoadingPage = refreshOrUploading ? 1 : mLoadingPage);
    }

    @Override
    public void onBindRepositoryItemViewAtPosition(int position, RepositoryItemView itemView) {
        RandomUser randomUser = mRandomUsers.get(position);
        itemView.setUserNumber(position);
        itemView.setMask(randomUser.getGender(), randomUser.getPicture().getLarge());
        itemView.setUserFullName(randomUser.getFullName());
    }

    @Override
    public int getRepositoryItemsCount() {
        return mRandomUsers.size();
    }

    @Override
    public void onDetachView() {
        mView = null;
    }

    @Override
    public void onResponse(Call<RandomUsers> call, Response<RandomUsers> response) {
        if (response.body() != null) {
            mLoadingPage++;
            setLoading(false);
            boolean wayUpdate;
            if (mRandomUsers == null | mLoadingPage <= 2) {
                wayUpdate = true;
                mRandomUsers = response.body().getResults();
            } else {
                wayUpdate = false;
                mRandomUsers.addAll(response.body().getResults());
            }

            mView.toggleListAndProgressBar(true);
            mView.updateUI(wayUpdate);
        }
    }

    @Override
    public void onFailure(Call<RandomUsers> call, Throwable t) {
        mView.onErrorFetchingData();
    }
}
