package stefanserkhir.randomuserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class RandomUsersActivity extends AppCompatActivity {

    private static final String TAG = "RandomUsersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_users);

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
