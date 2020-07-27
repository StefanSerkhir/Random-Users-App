package stefanserkhir.randomuserapp.presenter.helpers;

import android.os.AsyncTask;

import java.util.List;

import stefanserkhir.randomuserapp.model.RandomUser;

public class FetchRandomUsersTask extends AsyncTask<Void, Void, List<RandomUser>> {
    public interface AfterFetchUsers {
        void doAfterFetchUsers(List<RandomUser> randomUsers);
    }

    private AfterFetchUsers mCallback;
    private int mLoadingPage;

    public FetchRandomUsersTask(int loadingPage, AfterFetchUsers callback) {
        mLoadingPage = loadingPage;
        mCallback = callback;
    }

    @Override
    protected List<RandomUser> doInBackground(Void... voids) {
        return new RandomUsersFetcher().fetchRandomUsers(mLoadingPage);
    }

    @Override
    protected void onPostExecute(List<RandomUser> randomUsers) {
        mCallback.doAfterFetchUsers(randomUsers);
    }
}
