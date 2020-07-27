package stefanserkhir.randomuserapp.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import stefanserkhir.randomuserapp.R;
import stefanserkhir.randomuserapp.interfaces.ui.RandomUsersView;
import stefanserkhir.randomuserapp.presenter.RandomUsersPresenterImpl;
import stefanserkhir.randomuserapp.ui.helpers.RandomUserAdapter;
import stefanserkhir.randomuserapp.ui.helpers.ScrollToEndListener;

public class RandomUsersActivity extends AppCompatActivity implements RandomUsersView {
    private RandomUsersPresenterImpl randomUsersPresenter;
    private RecyclerView mRandomUsersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRandomUsersRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRefreshLayout = findViewById(R.id.random_users_container);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(() -> {
            reloadUsers();
            mRefreshLayout.setRefreshing(false);
        });

        reloadUsers();
    }

    private void reloadUsers() {
        randomUsersPresenter = new RandomUsersPresenterImpl(this);
        randomUsersPresenter.fetchRandomUsers();
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
            reloadUsers();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateUI(boolean wayUpdate) {
        if (wayUpdate) {
            mRandomUsersRecyclerView.setAdapter(new RandomUserAdapter(randomUsersPresenter,
                    RandomUsersActivity.this));
        } else {
            mRandomUsersRecyclerView.swapAdapter(new RandomUserAdapter(randomUsersPresenter,
                    RandomUsersActivity.this), false);
        }
    }
}