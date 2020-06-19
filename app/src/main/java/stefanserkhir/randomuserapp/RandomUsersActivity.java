package stefanserkhir.randomuserapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class RandomUsersActivity extends AppCompatActivity {

    private static final String TAG = "RandomUsersActivity";

    private RecyclerView mRandomUsersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

        mRandomUsersRecyclerView = findViewById(R.id.random_users_recycler_view);

        new FetchRandomUsersTask().execute();
    }

    private class FetchRandomUsersTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            new RandomUsersFetcher().fetchRandomUsers();
            return null;
        }
    }
}
