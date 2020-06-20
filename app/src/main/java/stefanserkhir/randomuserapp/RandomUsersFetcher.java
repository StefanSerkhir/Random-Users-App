package stefanserkhir.randomuserapp;

import android.net.Uri;

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

    public String getURLContent(String stringURL) throws IOException {
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
            return new String(out.toByteArray());
        } finally {
            connection.disconnect();
        }
    }

    public List<RandomUser> fetchRandomUsers(int page) {
        List<RandomUser> randomUsers = new ArrayList<>();

        try {
            String stringURL = Uri.parse("https://randomuser.me/api/")
                    .buildUpon()
                    .appendQueryParameter("results", "10")
                    .appendQueryParameter("page", String.valueOf(page))
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("noinfo", "1")
                    .build().toString();
            String jsonString = getURLContent(stringURL);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseUsers(randomUsers, jsonBody);
        } catch (IOException | JSONException e) {
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

            users.add(randomUser);
        }
    }
}
