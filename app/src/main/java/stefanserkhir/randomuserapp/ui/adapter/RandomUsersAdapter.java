package stefanserkhir.randomuserapp.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.interfaces.presenter.RandomUsersPresenter;

public class RandomUsersAdapter extends RecyclerView.Adapter {
    private static final int ITEM_VIEW = 1;
    private static final int ITEM_PROGRESS = 0;

    private final RandomUsersPresenter mPresenter;
    private Activity mActivity;

    public RandomUsersAdapter(RandomUsersPresenter presenter, Activity activity) {
        mPresenter = presenter;
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return (position + 1) != mPresenter
                .getRepositoryItemsCount() ? ITEM_VIEW : ITEM_PROGRESS;
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
            return new ProgressBarHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RandomUserHolder) {
            RandomUserHolder randomUserHolder = (RandomUserHolder) holder;
            mPresenter.onBindRepositoryItemViewAtPosition(position, randomUserHolder);
        }
    }

    @Override
    public int getItemCount() {
        return mPresenter.getRepositoryItemsCount();
    }
}
