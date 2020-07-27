package stefanserkhir.randomuserapp.ui.helpers;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.presenter.RandomUsersPresenterImpl;

public class RandomUserAdapter extends RecyclerView.Adapter {
    private static final int ITEM_VIEW = 1;
    private static final int ITEM_PROGRESS = 0;

    private final RandomUsersPresenterImpl mRandomUsersPresenter;
    private Activity mActivity;

    public RandomUserAdapter(RandomUsersPresenterImpl randomUsersPresenter, Activity activity) {
        mRandomUsersPresenter = randomUsersPresenter;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return (position + 1) != mRandomUsersPresenter.getRepositoriesItemsCount() ? ITEM_VIEW :
                                                                                    ITEM_PROGRESS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View itemView = mActivity.getLayoutInflater().inflate(R.layout.random_user_item,
                    parent, false);
            return new RandomUserHolder(itemView, mActivity);
        } else {
            View itemView = mActivity.getLayoutInflater().inflate(R.layout.progress_bar, parent, false);
            return new ProgressBarHolder(itemView, mActivity);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RandomUserHolder) {
            RandomUserHolder randomUserHolder = (RandomUserHolder) holder;
            mRandomUsersPresenter.onBindRepositoryItemViewAtPosition(position, randomUserHolder);
        } else {
            ProgressBarHolder progressBarHolder = (ProgressBarHolder) holder;
            progressBarHolder.bindProgressBarItem();
        }
    }

    @Override
    public int getItemCount() {
        return mRandomUsersPresenter.getRepositoriesItemsCount();
    }
}
