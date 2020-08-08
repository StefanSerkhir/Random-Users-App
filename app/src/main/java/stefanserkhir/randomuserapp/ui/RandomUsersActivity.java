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
import stefanserkhir.randomuserapp.ui.helpers.RandomUsersAdapter;
import stefanserkhir.randomuserapp.ui.helpers.ScrollToEndListener;

public class RandomUsersActivity extends AppCompatActivity implements RandomUsersView {
    private RandomUsersPresenter randomUsersPresenter;
    private RecyclerView mRandomUsersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayout mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mLoadingBar = findViewById(R.id.loading_bar);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRandomUsersRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRefreshLayout = findViewById(R.id.random_users_container);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(() ->
                randomUsersPresenter.onUpdatingList(true));

        randomUsersPresenter = RandomUsersPresenterImpl.getInstance();
        randomUsersPresenter.onAttachView(this);
        mRandomUsersRecyclerView.addOnScrollListener(new ScrollToEndListener(mLinearLayoutManager,
                randomUsersPresenter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.random_user_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.reload_users) {
            randomUsersPresenter.onUpdatingList(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(boolean wayUpdate) {
        if (wayUpdate) {
            mRandomUsersRecyclerView.setAdapter(new RandomUsersAdapter(randomUsersPresenter,
                    this));
        } else {
            mRandomUsersRecyclerView.swapAdapter(new RandomUsersAdapter(randomUsersPresenter,
                    this), false);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Snackbar.make(mRefreshLayout, R.string.error_load,
                BaseTransientBottomBar.LENGTH_INDEFINITE).setAction(R.string.retry,
                view -> randomUsersPresenter.onUpdatingList(false)).show();
    }

    @Override
    public void toggleOn(boolean toggle) {
        mRandomUsersRecyclerView.setVisibility(toggle ? View.VISIBLE : View.GONE);
        mLoadingBar.setVisibility(toggle ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        randomUsersPresenter.onDetachView();
    }
}