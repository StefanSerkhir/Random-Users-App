package stefanserkhir.randomuserapp.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import stefanserkhir.randomuserapp.interfaces.presenter.RandomUsersPresenter;

public class ScrollToEndListener extends RecyclerView.OnScrollListener  {
    private LinearLayoutManager mLinearLayoutManager;
    private RandomUsersPresenter mRandomUsersPresenter;

    public ScrollToEndListener(RecyclerView.LayoutManager linearLayoutManager,
                               RandomUsersPresenter randomUsersPresenter) {
        mLinearLayoutManager = (LinearLayoutManager) linearLayoutManager;
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
                mRandomUsersPresenter.onUpdatingList(false);
            }
        }
    }
}
