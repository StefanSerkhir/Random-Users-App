package stefanserkhir.randomuserapp.presenter;

import java.util.ArrayList;
import java.util.List;

import stefanserkhir.randomuserapp.interfaces.presenter.RandomUsersPresenter;
import stefanserkhir.randomuserapp.interfaces.ui.RandomUsersView;
import stefanserkhir.randomuserapp.interfaces.ui.RepositoryItemView;
import stefanserkhir.randomuserapp.model.RandomUser;
import stefanserkhir.randomuserapp.presenter.helpers.FetchRandomUsersTask;
import stefanserkhir.randomuserapp.presenter.helpers.FetchRandomUsersTask.AfterFetchUsers;
import stefanserkhir.randomuserapp.ui.RandomUsersActivity;

public class RandomUsersPresenterImpl implements RandomUsersPresenter, AfterFetchUsers {
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
        itemView.setMask(randomUser.getGender(), randomUser.getAvatarURL());
        itemView.setUserFullName(randomUser.getFullName());
    }

    public int getRepositoriesItemsCount() {
        return mRandomUsers.size();
    }

    @Override
    public void fetchRandomUsers() {
        new FetchRandomUsersTask(mLoadingPage, this).execute();
    }

    @Override
    public void doAfterFetchUsers(List<RandomUser> randomUsers) {
        mLoadingPage++;
        setLoading(false);
        boolean wayUpdate;
        if (mRandomUsers.size() == 0 | mLoadingPage <= 2) {
            wayUpdate = true;
            mRandomUsers = randomUsers;
        } else {
            wayUpdate = false;
            mRandomUsers.addAll(randomUsers);
        }

        mUpdateUICallback.updateUI(wayUpdate);
    }
}
