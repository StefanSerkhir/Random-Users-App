package stefanserkhir.randomuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RandomUsersActivity extends AppCompatActivity {

    private static final int ITEM_VIEW = 1;
    private static final int ITEM_PROGRESS = 0;

    private RecyclerView mRandomUsersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<RandomUser> mRandomUsers = new ArrayList<>();
    private int mLoadingPage;
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mLoadingPage = 1;

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRandomUsersRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRandomUsersRecyclerView.addOnScrollListener(new ScrollToEndListener());

        new FetchRandomUsersTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.random_user_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload_users:
                mLoadingPage = 1;
                new FetchRandomUsersTask().execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ScrollToEndListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemsCount = mLinearLayoutManager.getChildCount();
            int totalItemsCount = mLinearLayoutManager.getItemCount();
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (!mIsLoading) {
                if (visibleItemsCount + firstVisibleItem >= totalItemsCount) {
                    mIsLoading = true;
                    new FetchRandomUsersTask().execute();
                }
            }
        }
    }

    private class RandomUserHolder extends RecyclerView.ViewHolder {
        private TextView mUserNumberTextView;
        private ImageView mUserAvatarImageView;
        private TextView mUserFullNameTextView;

        public RandomUserHolder(@NonNull View itemView) {
            super(itemView);

            mUserNumberTextView = itemView.findViewById(R.id.user_number);
            mUserAvatarImageView = itemView.findViewById(R.id.user_avatar);
            mUserFullNameTextView = itemView.findViewById(R.id.user_full_name);
        }

        public void bindRandomUserItem(RandomUser item, int position) {
            mUserNumberTextView.setText(getString(R.string.user_number, position + 1));
            int rDrawable = item.getGender().equals("male") ?
                    R.drawable.star_shape : R.drawable.heart_shape;
            Picasso.get()
                    .load(item.getAvatarURL())
                    .transform(new AvatarTransformation(RandomUsersActivity.this,
                            rDrawable))
                    .placeholder(rDrawable)
                    .into(mUserAvatarImageView);
            mUserFullNameTextView.setText(item.getFullName());
        }
    }

    private class ProgressBarHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        public ProgressBarHolder(@NonNull View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.progress_bar);
        }

        public void bindProgressBarItem() {
            mProgressBar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                    PorterDuff.Mode.MULTIPLY);
            mProgressBar.setIndeterminate(true);
        }
    }

    private class RandomUserAdapter extends RecyclerView.Adapter {
        private List<RandomUser> mRandomUsers;

        public RandomUserAdapter(List<RandomUser> randomUsers) {
            mRandomUsers = randomUsers;
        }

        @Override
        public int getItemViewType(int position) {
            return (position + 1) != mRandomUsers.size() ? ITEM_VIEW : ITEM_PROGRESS;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW) {
                View itemView = getLayoutInflater().inflate(R.layout.random_user_item,
                        parent, false);
                return new RandomUserHolder(itemView);
            } else {
                View itemView = getLayoutInflater().inflate(R.layout.progress_bar, parent, false);
                return new ProgressBarHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof RandomUserHolder) {
                RandomUserHolder randomUserHolder = (RandomUserHolder) holder;
                RandomUser randomUser = mRandomUsers.get(position);
                randomUserHolder.bindRandomUserItem(randomUser, position);
            } else {
                ProgressBarHolder progressBarHolder = (ProgressBarHolder) holder;
                progressBarHolder.bindProgressBarItem();
            }
        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RandomUserHolder randomUserHolder, int position) {
//            RandomUser randomUser = mRandomUsers.get(position);
//            randomUserHolder.bindRandomUserItem(randomUser, position);
//        }

        @Override
        public int getItemCount() {
            return mRandomUsers.size();
        }
    }

    private class FetchRandomUsersTask extends AsyncTask<Void, Void, List<RandomUser>> {
        @Override
        protected List<RandomUser> doInBackground(Void... voids) {
            return new RandomUsersFetcher().fetchRandomUsers(mLoadingPage);
        }

        @Override
        protected void onPostExecute(List<RandomUser> randomUsers) {
            mLoadingPage++;
            mIsLoading = false;
            if (mRandomUsers.size() == 0 | mLoadingPage <= 2) {
                mRandomUsers = randomUsers;
                mRandomUsersRecyclerView.setAdapter(new RandomUserAdapter(mRandomUsers));
            } else {
                mRandomUsers.addAll(randomUsers);
                mRandomUsersRecyclerView.swapAdapter(new RandomUserAdapter(mRandomUsers), false);
            }

/*            mRandomUsers = randomUsers;
            if (mRandomUsersRecyclerView.getAdapter() == null) {
                mRandomUsersRecyclerView.setAdapter(new RandomUserAdapter(mRandomUsers));
            } else {
                mRandomUsersRecyclerView.swapAdapter(new RandomUserAdapter(mRandomUsers), false);
            }*/
        }
    }
}
