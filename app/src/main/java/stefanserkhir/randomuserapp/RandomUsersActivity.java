package stefanserkhir.randomuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RandomUsersActivity extends AppCompatActivity {

    private RecyclerView mRandomUsersRecyclerView;
    private List<RandomUser> mRandomUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));

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
                new FetchRandomUsersTask().execute();
            default:
                return super.onOptionsItemSelected(item);
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
            Picasso.get()
                    .load(item.getAvatarURL())
                    .transform(new AvatarTransformation(RandomUsersActivity.this,
                            item.getGender().equals("male") ?
                                    R.drawable.star_shape : R.drawable.heart_shape))
                    .placeholder(R.drawable.person_placeholder)
                    .into(mUserAvatarImageView);
            mUserFullNameTextView.setText(item.getFullName());
        }
    }

    private class RandomUserAdapter extends RecyclerView.Adapter<RandomUserHolder> {
        private List<RandomUser> mRandomUsers;

        public RandomUserAdapter(List<RandomUser> randomUsers) {
            mRandomUsers = randomUsers;
        }

        @NonNull
        @Override
        public RandomUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.random_user_item,
                                                            parent, false);
            return new RandomUserHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RandomUserHolder randomUserHolder, int position) {
            RandomUser randomUser = mRandomUsers.get(position);
            randomUserHolder.bindRandomUserItem(randomUser, position);
        }

        @Override
        public int getItemCount() {
            return mRandomUsers.size();
        }
    }

    private class FetchRandomUsersTask extends AsyncTask<Void, Void, List<RandomUser>> {
        @Override
        protected List<RandomUser> doInBackground(Void... voids) {
            return new RandomUsersFetcher().fetchRandomUsers();
        }

        @Override
        protected void onPostExecute(List<RandomUser> randomUsers) {
            mRandomUsers = randomUsers;
            if (mRandomUsersRecyclerView.getAdapter() == null) {
                mRandomUsersRecyclerView.setAdapter(new RandomUserAdapter(mRandomUsers));
            } else {
                mRandomUsersRecyclerView.swapAdapter(new RandomUserAdapter(mRandomUsers), false);
            }
        }
    }
}
