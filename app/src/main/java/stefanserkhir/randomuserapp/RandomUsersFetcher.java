package stefanserkhir.randomuserapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RandomUsersFetcher {
    private static final String TAG = "RandomUsersFetcher";

    public byte[] getURLBytes(String stringURL) throws IOException {
        URL url = new URL(stringURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with" + stringURL);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getURLContent(String stringURL) throws IOException {
        return new String(getURLBytes(stringURL));
    }

    public List<RandomUser> fetchRandomUsers() {
        List<RandomUser> randomUsers = new ArrayList<>();

        try {
            String stringURL = Uri.parse("https://randomuser.me/api/")
                    .buildUpon()
                    .appendQueryParameter("results", "40")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("noinfo", "1")
                    .build().toString();
            String jsonString = getURLContent(stringURL);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseUsers(randomUsers, jsonBody);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch users", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return randomUsers;
    }

    private void parseUsers(List<RandomUser> users, JSONObject jsonBody)
            throws JSONException {

        JSONArray usersJsonArray = jsonBody.getJSONArray("results");

        for (int i = 0; i < usersJsonArray.length(); i++) {
            JSONObject randomUserJsonObject = usersJsonArray.getJSONObject(i);
            JSONObject nameOfRandomUserJsonObject = randomUserJsonObject.getJSONObject("name");
            JSONObject avatarOfRandomUserJsonObject = randomUserJsonObject.getJSONObject("picture");

            RandomUser randomUser = new RandomUser();
            randomUser.setGender(randomUserJsonObject.getString("gender"));
            randomUser.setTitle(nameOfRandomUserJsonObject.getString("title"));
            randomUser.setFirstName(nameOfRandomUserJsonObject.getString("first"));
            randomUser.setLastName(nameOfRandomUserJsonObject.getString("last"));
            randomUser.setAvatarURL(avatarOfRandomUserJsonObject.getString("large"));
            Log.i(TAG, "Random user #" + i + ": gender = " + randomUser.getGender());
            Log.i(TAG, "Random user #" + i + ": title = " + randomUser.getTitle());
            Log.i(TAG, "Random user #" + i + ": first = " + randomUser.getFirstName());
            Log.i(TAG, "Random user #" + i + ": last = " + randomUser.getLastName());
            Log.i(TAG, "=================================================================");

            users.add(randomUser);
        }
    }
}
