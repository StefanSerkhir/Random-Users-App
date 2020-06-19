package stefanserkhir.randomuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RandomUsersActivity extends AppCompatActivity {

    private static final String TAG = "RandomUsersActivity";

    private RecyclerView mRandomUsersRecyclerView;
    private List<RandomUser> mRandomUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);
        mRandomUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        new FetchRandomUsersTask().execute();
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
            mUserNumberTextView.setText(position + ".");
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
            View itemView = getLayoutInflater().inflate(R.layout.random_user_item, parent, false);
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
            mRandomUsersRecyclerView.setAdapter(new RandomUserAdapter(mRandomUsers));
        }
    }
}
