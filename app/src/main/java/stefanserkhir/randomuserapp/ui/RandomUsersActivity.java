package stefanserkhir.randomuserapp.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.interfaces.presenter.RandomUsersPresenter;
import stefanserkhir.randomuserapp.interfaces.views.RandomUsersView;
import stefanserkhir.randomuserapp.presenter.RandomUsersPresenterImpl;
import stefanserkhir.randomuserapp.ui.adapter.RandomUsersAdapter;
import stefanserkhir.randomuserapp.ui.helpers.ScrollToEndListener;

public class RandomUsersActivity extends AppCompatActivity implements RandomUsersView {
    private RandomUsersPresenter mPresenter;
    private RecyclerView mRandomUsersRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayout mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mLoadingBar = findViewById(R.id.loading_bar);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setHasFixedSize(true);
        mRandomUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshLayout = findViewById(R.id.random_users_container);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(() ->
                mPresenter.onUpdatingList(true));

        mPresenter = RandomUsersPresenterImpl.getInstance();
        mPresenter.onAttachView(this);
        mRandomUsersRecyclerView.addOnScrollListener(new ScrollToEndListener(
                mRandomUsersRecyclerView.getLayoutManager(), mPresenter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.random_user_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.reload_users) {
            mPresenter.onUpdatingList(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(boolean wayUpdate) {
        if (wayUpdate) {
            mRandomUsersRecyclerView.setAdapter(new RandomUsersAdapter(mPresenter,
                    this));
        } else {
            mRandomUsersRecyclerView.swapAdapter(new RandomUsersAdapter(mPresenter,
                    this), false);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Snackbar.make(mRefreshLayout, R.string.error_load,
                BaseTransientBottomBar.LENGTH_INDEFINITE).setAction(R.string.retry,
                view -> mPresenter.onUpdatingList(false)).show();
    }

    @Override
    public void toggleOn(boolean toggle) {
        mRandomUsersRecyclerView.setVisibility(toggle ? View.VISIBLE : View.GONE);
        mLoadingBar.setVisibility(toggle ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.onDetachView();
    }
}