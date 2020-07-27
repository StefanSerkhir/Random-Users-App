package stefanserkhir.randomuserapp.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import stefanserkhir.randomuserapp.presenter.RandomUsersPresenterImpl;

public class ScrollToEndListener extends RecyclerView.OnScrollListener  {
    private LinearLayoutManager mLinearLayoutManager;
    private RandomUsersPresenterImpl mRandomUsersPresenter;

    public ScrollToEndListener(LinearLayoutManager linearLayoutManager,
                               RandomUsersPresenterImpl randomUsersPresenter) {
        mLinearLayoutManager = linearLayoutManager;
        mRandomUsersPresenter = randomUsersPresenter;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemsCount = mLinearLayoutManager.getChildCount();
        int totalItemsCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if (!mRandomUsersPresenter.isLoading()) {
            if (visibleItemsCount + firstVisibleItem >= totalItemsCount) {
                mRandomUsersPresenter.setLoading(true);
                mRandomUsersPresenter.fetchRandomUsers();
            }
        }
    }
}
